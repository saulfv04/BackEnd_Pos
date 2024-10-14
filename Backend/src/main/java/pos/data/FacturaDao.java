package pos.data;

import pos.logic.Factura;
import pos.logic.Fecha;
import pos.logic.Linea;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FacturaDao {
    Database db;

    public FacturaDao() {
        db = Database.instance();
    }
    public String generateFacturaId() throws Exception {
        String sequenceId;
        String newId;
        boolean idExists;

        do {
            String sql = "INSERT INTO FacturaSequence () VALUES ()";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.executeUpdate();
            ResultSet rs = stm.getGeneratedKeys();

            if (rs.next()) {
                sequenceId = String.format("F%03d", rs.getInt(1));
            } else {
                throw new SQLException("No se pudo generar un nuevo ID para la factura.");
            }

            sql = "SELECT COUNT(*) FROM Factura WHERE id = ?";
            stm = db.prepareStatement(sql);
            stm.setString(1, sequenceId);
            rs = stm.executeQuery();

            if (rs.next()) {
                idExists = rs.getInt(1) > 0;
            } else {
                throw new SQLException("Error al verificar la existencia del ID de la factura.");
            }
        } while (idExists);

        return sequenceId;
    }
    // Método para crear una nueva factura
    public void create(Factura factura) throws Exception {

        factura.setId(generateFacturaId());
        String sql = "INSERT INTO Factura (id, metodoPago, total, clienteId, cajeroId, fecha) VALUES (?, ?, ?, ?, ?, now())";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, factura.getId());
        stm.setString(2, factura.getMetodoPago());
        stm.setDouble(3, factura.getTotal());
        stm.setString(4, factura.getCliente().getId()); // Suponiendo que el Cliente tiene un método getId()
        stm.setString(5, factura.getCajero().getId()); // Suponiendo que el Cajero tiene un método getId()
        db.executeUpdate(stm);

//         También puedes agregar las líneas de la factura aquí si es necesario
        for (Linea linea : factura.getLinea()) {
            createLinea(factura.getId(), linea); // Método auxiliar para insertar líneas
        }
    }

    // Método para leer una factura por ID
    public Factura read(String id) throws Exception {
        String sql = "SELECT * FROM Factura WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            Factura factura = from(rs);
            factura.setLinea(getLineasByFacturaId(id)); // Cargar las líneas asociadas
            return factura;
        } else {
            throw new Exception("Factura NO EXISTE");
        }
    }

    // Método para actualizar una factura existente
    public void update(Factura factura) throws Exception {
        String sql = "UPDATE Factura SET metodoPago = ?, total = ?, clienteId = ?, cajeroId = ?, fecha = ? WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, factura.getMetodoPago());
        stm.setDouble(2, factura.getTotal());
        stm.setString(3, factura.getCliente().getId());
        stm.setString(4, factura.getCajero().getId());
        stm.setDate(5, java.sql.Date.valueOf(factura.getFecha().toString()));
        stm.setString(6, factura.getId());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Factura NO EXISTE");
        }

        // Aquí puedes actualizar las líneas si es necesario
    }

    // Método para eliminar una factura
    public void delete(Factura factura) throws Exception {
        String sql = "DELETE FROM Factura WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, factura.getId());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Factura NO EXISTE");
        }

        // Aquí puedes eliminar las líneas asociadas si es necesario
    }

    // Método para buscar facturas basadas en un criterio
    public List<Factura> search(Factura e) throws Exception {
        List<Factura> resultado = new ArrayList<>();
        String sql = "SELECT * FROM Factura WHERE metodoPago LIKE ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getMetodoPago() + "%");
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()) {
            Factura factura = from(rs);
            factura.setLinea(getLineasByFacturaId(factura.getId())); // Cargar las líneas asociadas
            resultado.add(factura);
        }

        return resultado;
    }

    // Método auxiliar para mapear un ResultSet a un objeto Factura
    private Factura from(ResultSet rs) throws Exception {
        Factura factura = new Factura();
        ClienteDao cliente = new ClienteDao();
        CajeroDao cajero = new CajeroDao();
        factura.setId(rs.getString("id"));
        factura.setMetodoPago(rs.getString("metodoPago"));
        factura.setTotal(rs.getDouble("total"));
        factura.setCliente(cliente.read(rs.getString("clienteId")));
        factura.setCajero(cajero.read(rs.getString("cajeroId")));
        factura.setFecha(new Fecha()); // Asegúrate de crear un objeto Fecha a partir de la fecha de la base de datos
        return factura;
    }

    // Método auxiliar para obtener las líneas asociadas a una factura
    private List<Linea> getLineasByFacturaId(String facturaId) throws Exception {
        // Implementa la lógica para obtener las líneas de la base de datos usando el id de la factura
        // Asegúrate de devolver una lista de objetos Linea
        return new ArrayList<>(); // Devuelve una lista de líneas obtenidas de la base de datos
    }

    // Método auxiliar para crear una línea asociada a una factura
    private void createLinea(String facturaId, Linea linea) throws Exception {
        // Implementa la lógica para crear una línea en la base de datos asociada a la factura
        String sql = "INSERT INTO Linea (id, descuento, cantidad, productoId, facturaId) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, linea.getIdLinea());
        stm.setDouble(2, linea.getDescuento());
        stm.setInt(3, linea.getCantidad());
        stm.setString(4, linea.getProducto().getCodigo());
        stm.setString(5, facturaId);
        db.executeUpdate(stm);


    }

    // Método para obtener facturas por año y mes
    public List<Factura> getByFecha(int anio, int mes) throws Exception {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT * FROM Factura WHERE YEAR(fecha) = ? AND MONTH(fecha) = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, anio);
        stm.setInt(2, mes);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()) {
            Factura factura = from(rs);
            factura.setLinea(getLineasByFacturaId(factura.getId())); // Cargar las líneas asociadas
            facturas.add(factura);
        }

        return facturas;
    }
}
