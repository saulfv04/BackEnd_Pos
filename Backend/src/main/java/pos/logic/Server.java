package pos.logic;

import pos.data.UsuarioDao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    ServerSocket srv;
    List<Worker> workers;
    public Server() {
        try {
            srv = new ServerSocket(Protocol.PORT);
            workers=Collections.synchronizedList(new ArrayList<Worker>());
            System.out.println("Servidor iniciado...");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void run() {
        Service service = new Service();
        boolean continuar = true;
        Socket s;
        Worker worker;
        String sid;  // Session Id

        while (continuar) {
            try {
                s = srv.accept();
                System.out.println("Conexión Establecida...");
                ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                int type = is.readInt();
                switch (type) {
                    case Protocol.SYNC:
                        sid = s.getRemoteSocketAddress().toString();
                        System.out.println("SYNCH: " + sid);
                        worker = new Worker(this, s, os, is, sid, Service.instance());
                        workers.add(worker);
                        System.out.println("Quedan: " + workers.size());
                        worker.start();
                        os.writeObject(sid);  // send Session Id back
                        break;

                    case Protocol.ASYNC:
                        sid = (String) is.readObject();  // receives Session Id
                        Usuarios usuarios = (Usuarios) is.readObject();
                        System.out.println("ASYNCH: " + sid);
                        System.out.println("Usuario Id: " + usuarios);
                        join(s, os, is, sid,usuarios);
                        notifyLogin(sid);
                        break;
                }
                os.flush();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void join(Socket as, ObjectOutputStream aos, ObjectInputStream ais, String sid,Usuarios usuario){
        for (Worker w: workers){
            if(w.sid.equals(sid)){
                w.setAs(as,aos,ais);
                w.setIdUsuario(usuario);
                break;
            }
        }
    }

    public void deliver_message(Worker from,String message) {
        for (Worker w: workers){
            if(w!=from) w.deliver_message(message);
        }
    }

    public void notifyLogin(String sid){
        for (Worker w: workers){
            w.notifyLogin();
        }
    }



    public List<Usuarios> getActiveUsers(String sid) {
        List<Usuarios> activeUsers = new ArrayList<>();

        // Verificar si la lista de workers es nula antes de iterar
        if (workers != null) {
            for (Worker worker : workers) {
                if (!Objects.equals(worker.getSessionId(), sid)) {  // Verificar que el sessionId no sea nulo
                    activeUsers.add(worker.getUsuario());
                }
            }
        } else {
            System.out.println("La lista de workers es nula.");
        }
        return activeUsers; // Devolver la lista de usuarios activos
    }

    public List<Usuarios> getAllActiveUsers() {
        List<Usuarios> activeUsers = new ArrayList<>();

        // Verificar si la lista de workers es nula antes de iterar
        if (workers != null) {
            for (Worker worker : workers) {
                // Verificar que worker y worker.getUsuario() no sean nulos
                if (worker != null && worker.getUsuario() != null) {
                    // Asegurarse de que el ID no esté vacío y que realmente sea un usuario activo
                    if (!worker.getUsuario().getId().isEmpty()) {
                        activeUsers.add(worker.getUsuario());
                        System.out.println("Usuario activo agregado: " + worker.getUsuario().getId());
                    }
                }
            }
        } else {
            System.out.println("La lista de workers es nula.");
        }

        System.out.println("Total de usuarios activos encontrados: " + activeUsers.size());
        return activeUsers; // Devolver la lista de usuarios activos
    }


    public Worker getWorkerByUsuario(Usuarios usuario) {
        for (Worker worker : workers) {
            if (worker.getUsuario().equals(usuario)) {
                return worker;
            }
        }
        return null;
    }
}

