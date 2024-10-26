package pos.logic;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Worker {
    Server srv;
    Socket s;
    ObjectInputStream is;
    ObjectOutputStream os;
    IService service;

    String sid;
    Socket as;
    ObjectOutputStream aos;
    ObjectInputStream ais;
    Usuarios usuario;

    public Worker(Server srv,Socket s, ObjectOutputStream os,ObjectInputStream is,String sid,  IService service) {
        this.service = service;
        this.s = s;
        this.srv = srv;
        this.os=os;
        this.is=is;
        this.sid=sid;
        this.usuario=null  ;
    }
    public void setAs(Socket as,ObjectOutputStream aos, ObjectInputStream ais){
        this.as=as;
        this.aos=aos;
        this.ais=ais;
    }

        boolean continuar ;
        public void start(){
            try{
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        listen();
                    }
                });
                continuar=true;
                t.start();
            }catch (Exception ex){
            }

        }
        public void stop(){
            continuar=false;
        }
        public void listen(){

            try {
                int method;
                while (continuar) {
                    method = is.readInt();
                    System.out.println("Operación: " + method);
                    switch (method) {
                        // Operaciones de Producto
                        case Protocol.PRODUCTO_CREATE:
                            try {
                                service.create((Producto) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Producto creado");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;
                        case Protocol.PRODUCTO_READ:
                            try {
                                Producto producto = service.read((Producto) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(producto);
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.PRODUCTO_UPDATE:
                            try {
                                service.update((Producto) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Producto actualizado");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.PRODUCTO_DELETE:
                            try {
                                service.delete((Producto) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Producto eliminado");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.PRODUCTO_SEARCH:
                            try {
                                List<Producto> productos = service.search((Producto) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(productos);
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CATEGORIAS_GETCATEGORIAS:
                            try {
                                // Llamar al método getAll() del servicio para obtener la lista de categorías
                                List<Categoria> categorias = service.getCategorias();

                                os.writeInt(Protocol.ERROR_NO_ERROR);

                                // Enviar la lista de categorías resultantes
                                os.writeObject(categorias);
                                os.flush();
                            } catch (Exception ex) {
                                // Enviar el código de error en caso de excepción
                                os.writeInt(Protocol.ERROR_ERROR);

                                // Imprimir el stacktrace para depurar
                                ex.printStackTrace();
                            }
                            break;

                        case Protocol.PRODUCTO_UPDATE_EXISTENCIAS:
                            try {
                                // Lee el objeto producto que se va a actualizar
                                Producto producto = (Producto) is.readObject();
                                service.updateExistencias(producto); // Llama al servicio para actualizar las existencias
                                os.writeInt(Protocol.ERROR_NO_ERROR); // Enviar respuesta de éxito
                                srv.deliver_message(this, "Existencias actualizadas");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR); // Enviar respuesta de error
                                ex.printStackTrace(); // Imprimir el stack trace para depuración
                            }
                            break;
                        // Operaciones de Cajero
                        case Protocol.CAJERO_CREATE:
                            try {
                                service.create((Cajero) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Cajero creado");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CAJERO_READ:
                            try {
                                Cajero cajero = service.read((Cajero) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(cajero);
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CAJERO_UPDATE:
                            try {
                                service.update((Cajero) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Cajero actualizado");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CAJERO_DELETE:
                            try {
                                service.delete((Cajero) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Cajero eliminado");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CAJERO_SEARCH:
                            try {
                                List<Cajero> cajeros = service.search((Cajero) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(cajeros);
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;
                        case Protocol.CLIENTE_CREATE:
                            try {
                                service.create((Cliente) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Cliente creado");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CLIENTE_READ:
                            try {
                                Cliente cliente = service.read((Cliente) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(cliente);
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CLIENTE_UPDATE:
                            try {
                                service.update((Cliente) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Cliente actualizadp");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CLIENTE_DELETE:
                            try {
                                service.delete((Cliente) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Cliente eliminado");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CLIENTE_SEARCH:
                            try {
                                List<Cliente> clientes = service.search((Cliente) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(clientes);
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;
                        // Operaciones de Factura
                        case Protocol.FACTURA_CREATE:
                            try {
                                service.create((Factura) is.readObject()); // Lee el objeto Factura del cliente y lo pasa al servicio
                                os.writeInt(Protocol.ERROR_NO_ERROR); // Envía respuesta de éxito
                                srv.deliver_message(this, "Factura creada");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR); // Envía respuesta de error en caso de excepción
                            }
                            break;

                        case Protocol.FACTURA_READ:
                            try {
                                Factura factura = service.read((Factura) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(factura);
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.FACTURA_UPDATE:
                            try {
                                service.update((Factura) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Factura actualizada");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.FACTURA_DELETE:
                            try {
                                service.delete((Factura) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Factura eliminada");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.FACTURA_SEARCH:
                            try {
                                Factura factura = (Factura) is.readObject(); // Leer el objeto factura a buscar
                                List<Factura> facturas = service.search(factura); // Llamar al servicio para buscar facturas
                                os.writeInt(Protocol.ERROR_NO_ERROR); // Enviar respuesta de éxito
                                os.writeObject(facturas); // Enviar la lista de facturas encontradas
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR); // Enviar respuesta de error
                                ex.printStackTrace(); // Imprimir el stack trace para depuración
                            }
                            break;

                        case Protocol.FACTURA_SEARCHFACTURAID:
                            try {
                                String facturaId = (String) is.readObject(); // Leer el ID de la factura del cliente
                                Factura factura = new Factura();
                                factura.setId(facturaId); // Suponiendo que Factura tiene un método setId
                                List<Linea> lineas = service.searchByFacturId(factura.getId()); // Llamar al servicio
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(lineas); // Enviar la lista de líneas asociadas a la factura
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;
                        // Operaciones de Linea
                        case Protocol.LINEA_CREATE:
                            try {
                                service.create((Linea) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Linea creada");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.LINEA_READ:
                            try {
                                Linea linea = service.read((Linea) is.readObject());
                                os.writeObject(linea);
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.LINEA_UPDATE:
                            try {
                                service.update((Linea) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Linea actualizada");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.LINEA_DELETE:
                            try {
                                service.delete((Linea) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Linea eliminada");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.LINEA_SEARCH:
                            try {
                                List<Linea> lineas = service.search((Linea) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(lineas);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;
                        case Protocol.USUARIO_CREATE:
                            try {
                                service.create((Usuarios) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Usuario creado");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.USUARIO_READ:
                            try {
                                Usuarios usuarios = service.read((Usuarios) is.readObject());
                                os.writeObject(usuarios);
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.USUARIO_DELETE:
                            try {
                                service.delete((Usuarios) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                srv.deliver_message(this, "Usuario eliminado");
                                os.flush();
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.USUARIO_SEARCH:
                            try {
                                List<Usuarios> usuarios = service.search((Usuarios) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(usuarios);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.ESTADISTICAS_GETFACTURAS:
                            try {

                                // 1. Leer los parámetros enviados por el cliente
                                List<Categoria> categorias = (List<Categoria>) is.readObject();  // leer las categorías
                                List<String> cols = (List<String>) is.readObject();              // leer las columnas
                                Rango rango = (Rango) is.readObject();                           // leer el rango

                                // 2. Llamar al método del servicio con los parámetros recibidos
                                float[][] resultado = service.estadisticas(categorias, cols, rango);

                                // 3. Enviar el resultado al cliente
                                os.writeInt(Protocol.ERROR_NO_ERROR); // indicar que no hubo error
                                os.writeObject(resultado);            // enviar el objeto resultado

                            } catch (Exception ex) {
                                // Manejo de errores en caso de que algo falle
                                os.writeInt(Protocol.ERROR_ERROR);
                                ex.printStackTrace(); // para depuración, puedes eliminar esto en producción
                            }
                            break;
                        case Protocol.EXIT:
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.workers.remove(this);
                            System.out.println("Quedan:  " + srv.workers.size());
                            stop();
                            srv.deliver_message(this, "Sesión cerrada");
                            srv.notifyLogin(sid);
                            break;
                        case Protocol.REQUEST_ACTIVE_USERS:
                            try {
                                List<Usuarios> activeUsers = srv.getActiveUsers(sid);
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(activeUsers);
                                os.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case Protocol.FACTURA_SEND:
                            try {
                                Factura factura = (Factura) is.readObject();
                                Usuarios usuario = (Usuarios) is.readObject();
                                Worker w = srv.getWorkerByUsuario(usuario);
                                if (w != null) {
                                    w.aos.writeInt(Protocol.FACTURA_RECEIVE);
                                    w.aos.writeObject(factura);
                                    w.aos.writeObject(this.usuario);
                                    w.aos.flush();
                                }
                            }catch(Exception ex){
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.USUARIO_VERIFICATION:
                            try{
                                List<Usuarios> activeUsers = srv.getAllActiveUsers();
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(activeUsers);
                                os.flush();
                            }catch(Exception ex){
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                    }
                }
                os.flush();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

    public void deliver_message(String message){
        if (as != null) {
            try {
                aos.writeInt(Protocol.DELIVER_MESSAGE); // Enviar código de mensaje
                aos.writeObject(message); // Enviar el mensaje
                aos.flush(); // Asegurarse de que se envíe el mensaje
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        }

    public String getSessionId() {
            return this.sid;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setIdUsuario(Usuarios idUsuario) {
            this.usuario = idUsuario;
    }

    public void notifyLogin() {
        try {
            if (as != null) {
                aos.writeInt(Protocol.NEW_CONNECTION);
                List<Usuarios> activeUsers = srv.getActiveUsers(sid);
                aos.writeObject(activeUsers);
                aos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
