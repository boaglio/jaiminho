package com.boaglio.jaiminho;

import com.sun.net.httpserver.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.concurrent.Executors;

public class EuQueroEvitarAFadiga {

    public static final int MAX_SIZE_DESCRICAO = 10;
    public static final String CONTENT_TYPE = "Content-Type";

    public static final String APPLICATION_JSON = "application/json";
    public static final int STATUS_OK = 200;
    public static final int STATUS_INVALID = 422;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int EMPTY_RESPONSE_LENGTH = -1;
    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    private static long contador = 0L;
    public static final int DEFAULT_HTTP_PORT = 9999;
    
    static Logger log = LoggerFactory.getLogger(EuQueroEvitarAFadiga.class.getSimpleName());
    public static void main(String[] args) throws IOException {

        var serverPortEnv = System.getenv("SERVER_PORT");
        var porta = DEFAULT_HTTP_PORT;
        if (Objects.nonNull(serverPortEnv) ) {
            porta = Integer.parseInt(serverPortEnv);
        }
        var server = HttpServer.create(new InetSocketAddress(porta), 0);
        server.createContext("/clientes", new ClientHandler());
        server.createContext("/actuator", new StatusHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        var databaseService = new DatabaseService();
        var version = databaseService.getVersion();
        log.info("-----------------------------------------------------------");
        log.info("⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
        log.info("⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠁⠀⠀⠈⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
        log.info("⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣇⠀⠀⠀⢀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
        log.info("⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠛⢷⣤⡶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
        log.info("⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠋⠀⠀⠀⠀⠀⠀⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
        log.info("⣿⣿⣿⣿⣿⣿⣿⣿⠋⠀⠀⡀⠀⠀⠀⠀⠀⣿⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
        log.info("⣿⣿⣿⣿⣿⣿⣿⡏⠀⢰⣿⡇⠀⠀⠀⠀⢸⡇⠈⠛⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
        log.info("⣿⣿⣿⣿⣿⣿⣿⠃⠀⣾⣿⡇⠀⠀⠀⠀⣾⣿⣦⣄⡀⠀⠉⣿⣿⣿⣿⣿⣿⣿");
        log.info("⣿⣿⣿⣿⣿⣿⣿⣤⣴⣿⣿⣇⠀⠀⠀⢰⣿⣿⣿⣿⣿⣷⣶⣿⣿⣿⣿⣿⣿⣿");
        log.info("⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠻⣦⠀⠀⠈⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
        log.info("⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠀⠙⣷⡀⠀⠀⠙⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
        log.info("⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⠀⢠⣿⣿⣄⠀⠀⠘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
        log.info("⣿⣿⣿⣿⣿⣿⣿⣿⠟⠁⠀⣠⣾⣿⣿⣿⣧⡀⠀⠘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
        log.info("⣿⣿⣿⣿⣿⣿⣿⡁⠀⣠⣾⣿⣿⣿⣿⣿⣿⣷⡄⠀⣸⣿⣿⣿⣿⣿⣿⣿⣿⣿");
        log.info("⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
        log.info("-----------------------------------------------------------");
        log.info("Banco de dados: "+version);
        log.info("-----------------------------------------------------------");
        log.info("HTTP escutando na porta "+porta);
        log.info("-----------------------------------------------------------");
        log.info(" Corre Jaiminho !!! ");
        log.info("-----------------------------------------------------------");
    }

    static class StatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            var path = exchange.getRequestURI().getPath();
            var method = exchange.getRequestMethod();
            if (method.equalsIgnoreCase(METHOD_GET) && path.matches("^/actuator/health$")) {
    //            log.info((++contador) + " GET  PATH:  " + path);
                var response = "{\"status\":\"up\"}";
                exchange.getResponseHeaders().set(CONTENT_TYPE, APPLICATION_JSON);
                exchange.sendResponseHeaders(STATUS_OK, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }

    static class ClientHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            var path = exchange.getRequestURI().getPath();
            var method = exchange.getRequestMethod();
            if (method.equalsIgnoreCase(METHOD_POST) && path.matches("^/clientes/\\d+/transacoes$")) {
    //            log.info( (++contador) + " POST PATH:  "+path);
                handlePostTransaction(exchange);
            } else if (method.equalsIgnoreCase(METHOD_GET) && path.matches("^/clientes/\\d+/extrato$")) {
     //           log.info( (++contador) + " GET  PATH:  "+path);
                handleGetClient(exchange);
            } else {
      //          log.info( (++contador) + " Not Found PATH:  "+path);
                exchange.sendResponseHeaders(STATUS_NOT_FOUND, EMPTY_RESPONSE_LENGTH);
            }
        }

        private void handleGetClient(HttpExchange exchange) throws IOException {
            var path = exchange.getRequestURI().getPath();
            var parts = path.split("/");
            String id;
            if (parts.length >= 3) {
                id = parts[2];
                if (Objects.isNull(id)||
                        id.isEmpty() ||
                        Cliente.invalidCustomer(Integer.parseInt(id))
                ) {
                    // log.info(contador+" GET Client Not Found ! ");
                    exchange.sendResponseHeaders(STATUS_NOT_FOUND, EMPTY_RESPONSE_LENGTH);
                    return;
                }
                var databaseService = new DatabaseService();
                var clienteArmazenado = databaseService.findById(Integer.parseInt(id));
                var transacoes = databaseService.getLast10transactions(Integer.parseInt(id));
                var responseStr = new StringBuilder();
                responseStr.append("{\"saldo\":{\"total\":");
                responseStr.append(clienteArmazenado.getSaldo());
                responseStr.append(",\"data_extrato\":\"");
                responseStr.append(ZonedDateTime.now());
                responseStr.append("\",\"limite\":") ;
                responseStr.append(clienteArmazenado.getLimite());
                responseStr.append("},");
                responseStr.append("\"ultimas_transacoes\":[");
                var transacaoCount = 1;
                var totalTransacoes = transacoes.size();
                for (Transacao transacao : transacoes) {
                    responseStr.append("{\"valor\":");
                    responseStr.append(transacao.getValor());
                    responseStr.append(",\"tipo\":\"");
                    responseStr.append(transacao.getTipo());
                    responseStr.append("\",\"descricao\":\"");
                    responseStr.append(transacao.getDescricao().trim());
                    responseStr.append("\",\"realizada_em\":\"");
                    responseStr.append(transacao.getRealizadaEm().trim());
                    responseStr.append("\"}");
                    if (transacaoCount != totalTransacoes) {
                        responseStr.append(",");
                    }
                    transacaoCount++;
                }
                responseStr.append("]}");
                var response =  responseStr.toString();
                exchange.getResponseHeaders().set(CONTENT_TYPE, APPLICATION_JSON);
                exchange.sendResponseHeaders(STATUS_OK, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                exchange.sendResponseHeaders(STATUS_BAD_REQUEST, EMPTY_RESPONSE_LENGTH); // Bad Request
            }
            // log.info(contador+" GET Ok ");
        }

        private void handlePostTransaction(HttpExchange exchange) throws IOException {

            var path = exchange.getRequestURI().getPath();
            var parts = path.split("/");
            var  id = parts[2];

            var  requestBody = new StringBuilder();
            try (var reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            }
            // log.info( contador + " POST  Input:  "+requestBody);
            long valorDaTransacao;
            String tipo;
            String descricao;
            try {
                var jsonBody = new JSONObject(requestBody.toString());
                valorDaTransacao = jsonBody.getLong("valor");
                var valorDaTransacaoDouble = jsonBody.getDouble("valor");
                if (valorDaTransacaoDouble>valorDaTransacao) {
                    // se mandar valor quebrado
                    throw  new JSONException("invalid valor");
                }
                tipo = jsonBody.getString("tipo");
                descricao = jsonBody.getString("descricao");
            } catch  (JSONException je) {
                // log.info(contador+" POST Invalid JSON request  ! ");
                exchange.sendResponseHeaders(STATUS_INVALID, EMPTY_RESPONSE_LENGTH);
                return;
            }
            if (Objects.isNull(exchange.getRequestBody())||
                    Cliente.invalidCustomer(Integer.parseInt(id)) || // ID cliente
                    Objects.isNull(tipo) || tipo.isEmpty() ||  !Transacao.validTipoTransacao(tipo)  ||  // tipo
                    Objects.isNull(descricao)|| descricao.isEmpty() || descricao.length()> MAX_SIZE_DESCRICAO ||    // descricao
                    valorDaTransacao <= 0 // somente valores positivos
            ) {
                // log.info(contador+" POST Invalid request ! ");
                exchange.sendResponseHeaders(STATUS_INVALID, EMPTY_RESPONSE_LENGTH);
                return;
            }
            var databaseService = new DatabaseService();
            String response;
            try {
                var transacaoResponse = databaseService.efetuaTransacao(Integer.parseInt(id),valorDaTransacao,   tipo,   descricao);
                response = "{\"limite\":"+transacaoResponse.limite()+",\"saldo\":"+transacaoResponse.saldo()+"}";
                exchange.getResponseHeaders().set(CONTENT_TYPE, APPLICATION_JSON);
                exchange.sendResponseHeaders(STATUS_OK, response.getBytes().length);
            } catch (SemSaldoException e) {
                 // sem saldo
                // log.info(contador+" sem saldo ! ");
                exchange.sendResponseHeaders(STATUS_INVALID, EMPTY_RESPONSE_LENGTH);
                return;
            }

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            // log.info(contador+" POST Ok ");
        }
    }
}