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

        var porta = DEFAULT_HTTP_PORT;
        if (args.length>0) {
            porta = Integer.parseInt(args[0]);
        }
        var server = HttpServer.create(new InetSocketAddress(porta), 0);
        server.createContext("/clientes", new ClientHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        log.info("Server is listening on port "+porta);
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
        log.info("Corre Jaiminho!");
        log.info("-----------------------------------------------------------");
    }

    static class ClientHandler implements HttpHandler {

        public static final String APPLICATION_JSON = "application/json";

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            var path = exchange.getRequestURI().getPath();
            var method = exchange.getRequestMethod();
            if (method.equalsIgnoreCase(METHOD_POST) && path.matches("^/clientes/\\d+/transacoes$")) {
                handlePostTransaction(exchange);
            } else if (method.equalsIgnoreCase(METHOD_GET) && path.matches("^/clientes/\\d+/extrato$")) {
                handleGetClient(exchange);
            } else {
                exchange.sendResponseHeaders(STATUS_NOT_FOUND, EMPTY_RESPONSE_LENGTH); // Not Found
            }
        }

        private void handleGetClient(HttpExchange exchange) throws IOException {
            var path = exchange.getRequestURI().getPath();
            var parts = path.split("/");
            String id;
            if (parts.length >= 3) {
                id = parts[2];
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
        }

        private void handlePostTransaction(HttpExchange exchange) throws IOException {

            var path = exchange.getRequestURI().getPath();
      //      log.info(" Input PATH:  "+path);
            var parts = path.split("/");
            var  id = parts[2];

            var  requestBody = new StringBuilder();
            try (var reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            }
        //    log.info(" Input POST:  "+requestBody);
            long valorDaTransacao;
            String tipo;
            String descricao;
            try {
                var jsonBody = new JSONObject(requestBody.toString());
                valorDaTransacao = jsonBody.getLong("valor");
                tipo = jsonBody.getString("tipo");
                descricao = jsonBody.getString("descricao");
            } catch  (JSONException je) {
                log.info(++contador+" Invalid request ! ");
                exchange.sendResponseHeaders(STATUS_INVALID, EMPTY_RESPONSE_LENGTH);
                return;
            }
            if (Objects.isNull(exchange.getRequestBody())||
                    Cliente.invalidCustomer(Integer.parseInt(id)) || // ID cliente
                    Objects.isNull(tipo) || tipo.isEmpty() ||  !Transacao.validTipoTransacao(tipo)  ||  // tipo
                    Objects.isNull(descricao)|| descricao.isEmpty() || descricao.length()> MAX_SIZE_DESCRICAO ||    // descricao
                    valorDaTransacao <= 0 // somente valores positivos
            ) {
                log.info(++contador+" Invalid request ! ");
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
                exchange.sendResponseHeaders(STATUS_INVALID, EMPTY_RESPONSE_LENGTH);
                return;
            }

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }

        }
    }
}