import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * Classe responsavel por enviar itens à fila
 */
public class Produtor {

    public static void main(String[] args) throws Exception {
        //Criação de uma factory de conexão, responsável por criar as conexões
        ConnectionFactory connectionFactory = new ConnectionFactory();

        //Localização do gestor da fila (Queue Manager)
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);

        String NOME_FILA = "filaOla";
        try (
                //Criação de uma conexão
                Connection connection = connectionFactory.newConnection();
                //Criando um canal na conexão
                Channel channel = connection.createChannel()) {
                //Esse corpo especifica o envio da mensagem para a fila

            //Declaração da fila. Se não existir ainda no queue manager, será criada. Se já existir, e foi criada com
            //os mesmos parametros, pega a referência da fila. Se foi criada com parâmetros diferentes, lança exceção
            channel.queueDeclare ("fila", true, false, false, null);
            String mensagem = String.join (" ", args);
            mensagem += " Pedro Xavier";
            //Publica uma mensagem na fila
            channel.basicPublish ("", NOME_FILA,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    mensagem.getBytes());
            System.out.println ("[x] Enviado '" + mensagem + "'");
        }
    }
}
