package com.boaglio.jaiminho;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    private static final HikariDataSource dataSource;

    static final int CONNECTION_POOL_SIZE = 20;
    static final String DEBITO = "d";
    static final  String JDBC_URL = "jdbc:h2:tcp://localhost/rinha";
    static final String QUERY_FIND_CLIENTE_BY_ID = "SELECT limite, saldo FROM cliente WHERE id = ?";
    static final String QUERY_FIND_TRANSACAO_BY_CLIENTE_ID = "SELECT id, cliente_id, valor, tipo, descricao, realizadaEm FROM transacao WHERE cliente_id = ? ORDER BY realizadaEm DESC LIMIT 10";
    static final String INSERT_TRANSACAO = "INSERT INTO transacao (cliente_id, valor, tipo, descricao, realizadaEm) VALUES (?, ?, ?, ?, ?)";
    static final String UPDATE_SALDO_CLIENTE = "UPDATE cliente SET saldo = ? WHERE id = ?";
    static long contador = 0L;

    static Logger log = LoggerFactory.getLogger(DatabaseService.class.getSimpleName());

    static {
        var config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setMaximumPoolSize(CONNECTION_POOL_SIZE);

        dataSource = new HikariDataSource(config);
    }

    public Cliente findById(int id) {

        try (var connection = getConnection()) {

           var preparedStatement = connection.prepareStatement(QUERY_FIND_CLIENTE_BY_ID);
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            resultSet.next();
            connection.commit();
            var limite = resultSet.getInt("limite");
            var saldo = resultSet.getInt("saldo");
//            log.info(++contador +" ID: " + id + ", Limite: " + limite + ", Saldo: " + saldo);
            return  new Cliente(id,limite,saldo);

        } catch (SQLException e) {
            log.info("Connection failed!: "+e.getMessage());
        }
        return null;
    }


    public List<Transacao> getLast10transactions(int clienteId) {

        var transactions = new ArrayList<Transacao>();
        try (var connection = getConnection()) {
                try (var preparedStatement = connection.prepareStatement(QUERY_FIND_TRANSACAO_BY_CLIENTE_ID)) {
                    preparedStatement.setInt(1, clienteId);
                    var resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        var valor = resultSet.getLong("valor");
                        var tipo = resultSet.getString("tipo");
                        var descricao = resultSet.getString("descricao");
                        var realizadaEm = resultSet.getString("realizadaEm");
                        var transacao = new Transacao(valor, tipo, descricao, realizadaEm);
                        transactions.add(transacao);
                    }
                    connection.commit();
                }
        } catch (SQLException e) {
            log.info("SQL error!: "+e.getMessage());
        }

        return transactions;
    }

    public TransacaoResponse efetuaTransacao(int clientId, long valorDaTransacao, String tipo, String descricao) throws SemSaldoException {

        try (var connection = getConnection()) {
            var preparedStatement = connection.prepareStatement(QUERY_FIND_CLIENTE_BY_ID);
            // le saldo e limite
            preparedStatement.setInt(1, clientId);
            var resultSet = preparedStatement.executeQuery();
            resultSet.next();
            var limiteDoCliente = resultSet.getLong("limite");
            var saldoAtual = resultSet.getLong("saldo");

            if (DEBITO.equals(tipo)) {
                if ((saldoAtual + limiteDoCliente) >= valorDaTransacao) {
                    saldoAtual -= valorDaTransacao;
                    log.info(++contador + " Debito: " + valorDaTransacao + " saldo: " + saldoAtual + " limite: " + limiteDoCliente + " - cliente: " + clientId);
                } else {
                    log.info(++contador + " Sem saldo - cliente: " + clientId);
                    throw new SemSaldoException();
                }
            } else {
                // credito
                saldoAtual+=valorDaTransacao;
                log.info(++contador + " Credito - cliente: " + valorDaTransacao + " saldo: "+ saldoAtual + " limite: " +limiteDoCliente +  "- cliente: "+clientId);
            }
            // cadastra transacao
            log.info("ID: " + clientId + ", Limite: " + limiteDoCliente + ", Saldo: " + saldoAtual);
            var statement = connection.prepareStatement(INSERT_TRANSACAO);
            statement.setLong(1, clientId);
            statement.setLong(2, valorDaTransacao);
            statement.setString(3, tipo);
            statement.setString(4, descricao);
            statement.setString(5, ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
            statement.executeUpdate();
            // atualiza saldo
            statement = connection.prepareStatement(UPDATE_SALDO_CLIENTE);
            statement.setLong(1, saldoAtual);
            statement.setInt(2, clientId);
            statement.executeUpdate();

            connection.commit();

            return new TransacaoResponse(limiteDoCliente,saldoAtual);

        } catch (SQLException e) {
                log.info("SQL error!: "+e.getMessage());
        }
        return null;
    }

    private static Connection getConnection()   {
        try {
            //  sem connection pool
            //   return DriverManager.getConnection(JDBC_URL);
            var connection =  dataSource.getConnection();
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}