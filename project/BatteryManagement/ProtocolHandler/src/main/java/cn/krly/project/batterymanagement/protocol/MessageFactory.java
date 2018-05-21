package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/15.
 */
public class MessageFactory {

    public static Message newMessage(byte[] data) {
        if (data == null
                || data.length < 4)
            return null;

        // HeartBeat
        if (data.length == 4)
            return new ClientHeartBeatRequest();

        //
        int cmd = data[Message.KEY_POSITION] & 0xFF;
        Message message = null;
        switch (cmd) {
            case Message.MSG_ID_GET_SERVER_REQUEST: {
                message = new ClientGetServerRequest();
                break;
            }
            case Message.MSG_ID_CLIENT_QUERY_STATUS_RESPONSE: {
                message = new ClientQueryDeviceStatusResponse();
                break;
            }
            case Message.MSG_ID_CLIENT_QUERY_VERSION_RESPONSE: {
                message = new ClientQueryDeviceVersionResponse();
                break;
            }
            case Message.MSG_ID_CLIENT_SYNC_TIME_REQUEST: {
                message = new ClientSyncTimeRequest();
                break;
            }
            case Message.MSG_ID_CLIENT_GET_RANDOM_KEY_RESPONSE: {
                message = new ClientGetRandomKeyResponse();
                break;
            }
            case Message.MSG_ID_CLIENT_CHECK_SECRET_RESPONSE: {
                message = new ClientCheckSecretResponse();
                break;
            }
            case Message.MSG_ID_CLIENT_RENT_RESPONSE: {
                message = new ClientBatteryRentResponse();
                break;
            }
            case Message.MSG_ID_CLIENT_RETURN_REQUEST: {
                message = new ClientBatteryReturnRequest();
                break;
            }
            case Message.MSG_ID_CLIENT_UPDATE_RANDOM_REQUEST: {
                message = new ClientUpdateRandomRequest();
                break;
            }
            case Message.MSG_ID_CLIENT_CHECK_SECRET_REQUEST: {
                message = new ClientCheckSecretRequest();
                break;
            }
        }

        if (message != null) {
            if (message.isFromClient(data))
                return null;

            if (message.init(data))
                return message;
        }

        return null;
    }

    public static ServerGetServerResponse newGetServerResponse(String host, int port) {
        ServerGetServerResponse response = new ServerGetServerResponse();
        response.setHost(host);
        response.setPort(port);

        return response;
    }

    public static ServerHeartBeatResponse newHeartBeatResponse() {
        ServerHeartBeatResponse response = new ServerHeartBeatResponse();
        return response;
    }

    public static ServerQueryDeviceStatusRequest newQueryDeviceInfoRequest() {
        ServerQueryDeviceStatusRequest request = new ServerQueryDeviceStatusRequest();
        return request;
    }

    public static ServerQueryDeviceVersionRequest newQueryDeviceVersionRequest() {
        ServerQueryDeviceVersionRequest request = new ServerQueryDeviceVersionRequest();
        return request;
    }

    public static ServerSyncTimeResponse newSyncTimeResponse(long timeMills) {
        ServerSyncTimeResponse response = new ServerSyncTimeResponse();
        response.setTimeMills(timeMills);

        return response;
    }

    public static ServerGetRandomKeyRequest newGetRandomKeyRequest(int transactionId) {
        ServerGetRandomKeyRequest request = new ServerGetRandomKeyRequest();
        request.setTransactionId(transactionId);

        return request;
    }

    public static ServerCheckSecretRequest newCheckSecretRequest(int transactionId,
                                                                 byte[] randomKey,
                                                                 byte[] mac) {
        ServerCheckSecretRequest request = new ServerCheckSecretRequest();
        request.setTransactionId(transactionId);
        request.setRandomKey(randomKey);
        request.setMac(mac);

        return request;
    }

    public static ServerBatteryRentRequest newServerBatteryRentRequest(int transactionId) {
        ServerBatteryRentRequest request = new ServerBatteryRentRequest();
        request.setTransactionId(transactionId);

        return request;
    }

    public static ServerBatteryReturnResponse newServerBatteryReturnResponse(int transactionId,
                                                                             byte[] batteryId,
                                                                             byte status) {
        ServerBatteryReturnResponse response = new ServerBatteryReturnResponse();
        response.setTransactionId(transactionId);
        response.setBatteryId(batteryId);
        response.setStatus(status);

        return response;
    }

    public static ServerBatteryRentConfirmResponse newServerBatteryRentConfirmResponse(int transactionId,
                                                                                       byte[] batteryId) {
        ServerBatteryRentConfirmResponse response = new ServerBatteryRentConfirmResponse();
        response.setTransactionId(transactionId);
        response.setBatteryId(batteryId);

        return response;
    }

    public static ServerUpdateRandomResponse newServerUpdateRandomResponse(int transactionId) {
        ServerUpdateRandomResponse response = new ServerUpdateRandomResponse();
        response.setTransactionId(transactionId);

        return response;
    }

    public static ServerCheckSecretResponse newServerCheckSecretResponse(int transactionId,
                                                                         byte status) {
        ServerCheckSecretResponse response = new ServerCheckSecretResponse();
        response.setTransactionId(transactionId);
        response.setStatus(status);

        return response;
    }
}
