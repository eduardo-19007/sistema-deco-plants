package com.decoplants.sistema_web.repositories;

import com.decoplants.sistema_web.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    // Spring Data JPA ya incluye el método .save() por defecto.
    // Si el ID del objeto Pedido es nulo, .save() hará un INSERT. 
    // Si el ID ya existe en la base de datos, .save() hará un UPDATE.
}