package com.thief.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class AccountIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        byte[] array = new byte[20];
        Random random = new Random();
        random.nextBytes(array);

        StringBuilder builder = new StringBuilder();
        for (byte i : array)
            builder.append(String.format("%02X", i));

        return builder.toString().toLowerCase();
    }
}
