package server;

import service.IService;

import java.net.Socket;

public class ConcurrentServer extends AbsConcurrentServer{
    private IService server;

    public ConcurrentServer(int port, IService server) {
        super(port);
        this.server = server;
    }

    @Override
    protected Thread createWorker(Socket client) {
        ServerWorker worker=new ServerWorker(server,client);

        Thread tw=new Thread(worker);
        return tw;
    }
}
