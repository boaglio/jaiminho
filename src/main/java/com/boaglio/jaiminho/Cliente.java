package com.boaglio.jaiminho;

import java.util.Objects;

public class Cliente {
    private Integer id;
    private long limite;
    private long saldo;

    public Cliente() {}

    public Cliente(Integer id, long limite, long saldo) {
        this.id = id;
        this.limite = limite;
        this.saldo = saldo;
    }

    public static boolean invalidCustomer(long idCustomer) {
        return idCustomer <= 0 || idCustomer >= 6;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getLimite() {
        return limite;
    }

    public void setLimite(long limite) {
        this.limite = limite;
    }

    public long getSaldo() {
        return saldo;
    }

    public void setSaldo(long saldo) {
        this.saldo = saldo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", limite=" + limite +
                ", saldo=" + saldo +
                '}';
    }
}