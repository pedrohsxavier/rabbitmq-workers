import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class Consumidor {
    public static void main(String[] args) throws Exception {
        System.out.println("Consumidor");

        String NOME_FILA = "filaOla";

        //Criando a fábrica de conexões e criando uma conexão
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection conexao = connectionFactory.newConnection();

        //Criando um canal e declarando uma fila
        Channel canal = conexao.createChannel();
        canal.queueDeclare (NOME_FILA, true, false, false, null);

        System.out.println ("[*] Aguardando mensagens. Para sair, pressione CTRL + C");

        int prefetchCount = 1;
        canal.basicQos(prefetchCount);

        //Definindo a função callback
        DeliverCallback callback = (consumerTag, delivery) -> {
            String mensagem = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[x] Recebido '" + mensagem + "'");

            try {
                doWork(mensagem);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("[x] Feito");
                canal.basicAck(delivery.getEnvelope(). getDeliveryTag(), false);
            }
        };
        boolean autoAck = false;

        //Consome da fila
            canal.basicConsume(NOME_FILA, autoAck, callback, consumerTag -> {
        });
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}
