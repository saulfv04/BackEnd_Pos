package pos.logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Worker {
    Service service;
    Socket s;
    ServerSocket srv;
    public Worker(ServerSocket srv,Socket s, Service service) {
        this.service = service;
        this.s = s;
        this.srv = srv;
    }

        boolean continuar ;
        public void start(){
            try{
                Thread t = new Thread(new Runnable() {
                    @Override
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
                ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
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
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;
                        case Protocol.PRODUCTO_READ:
                            try {
                                Producto producto = service.read((Producto) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(producto);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.PRODUCTO_UPDATE:
                            try {
                                service.update((Producto) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.PRODUCTO_DELETE:
                            try {
                                service.delete((Producto) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.PRODUCTO_SEARCH:
                            try {
                                List<Producto> productos = service.search((Producto) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(productos);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CATEGORIAS_GETCATEGORIAS:
                            try {
                                // Llamar al método getAll() del servicio para obtener la lista de categorías
                                List<Categoria> categorias = service.getCategorias();

                                // Enviar el código de error si todo está bien
                                os.writeInt(Protocol.ERROR_NO_ERROR);

                                // Enviar la lista de categorías resultantes
                                os.writeObject(categorias);

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
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CAJERO_READ:
                            try {
                                Cajero cajero = service.read((Cajero) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(cajero);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CAJERO_UPDATE:
                            try {
                                service.update((Cajero) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CAJERO_DELETE:
                            try {
                                service.delete((Cajero) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CAJERO_SEARCH:
                            try {
                                List<Cajero> cajeros = service.search((Cajero) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(cajeros);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CLIENTE_READ:
                            try {
                                Cliente cliente = service.read((Cliente) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(cliente);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CLIENTE_UPDATE:
                            try {
                                service.update((Cliente) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CLIENTE_DELETE:
                            try {
                                service.delete((Cliente) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.CLIENTE_SEARCH:
                            try {
                                List<Cliente> clientes = service.search((Cliente) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(clientes);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;
                        // Operaciones de Factura
                        case Protocol.FACTURA_CREATE:
                            try {
                                service.create((Factura) is.readObject()); // Lee el objeto Factura del cliente y lo pasa al servicio
                                os.writeInt(Protocol.ERROR_NO_ERROR); // Envía respuesta de éxito
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR); // Envía respuesta de error en caso de excepción
                            }
                            break;

                        case Protocol.FACTURA_READ:
                            try {
                                Factura factura = service.read((Factura) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(factura);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.FACTURA_UPDATE:
                            try {
                                service.update((Factura) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.FACTURA_DELETE:
                            try {
                                service.delete((Factura) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
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
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.LINEA_DELETE:
                            try {
                                service.delete((Linea) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
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
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.USUARIO_READ:
                            try {
                                Usuarios usuarios = service.read((Usuarios) is.readObject());
                                os.writeObject(usuarios);
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                            } catch (Exception ex) {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                            break;

                        case Protocol.USUARIO_DELETE:
                            try {
                                service.delete((Usuarios) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
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
                    }
                    os.flush();

                }
                os.flush();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }



}
