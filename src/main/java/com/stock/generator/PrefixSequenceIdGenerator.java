package com.stock.generator;

import com.stock.generator.IdSequence;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

import org.hibernate.query.Query;
import java.io.Serializable;
import java.util.EnumSet;

public class PrefixSequenceIdGenerator implements BeforeExecutionGenerator {
    private String prefix = "GEN";

    public PrefixSequenceIdGenerator() {}

    public PrefixSequenceIdGenerator(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object entity, Object currentValue, EventType eventType) {
        try {
            // 1️⃣ Fetch IdSequence entity using HQL
            Query<IdSequence> query = session.createQuery(
                    "FROM IdSequence s WHERE s.entityName = :name", IdSequence.class);
            query.setParameter("name", entity.getClass().getSimpleName());
            query.setLockOptions(new LockOptions(LockMode.PESSIMISTIC_WRITE));

            IdSequence seq = query.uniqueResult();

            if (seq == null) {
                throw new RuntimeException("IdSequence not initialized for " + entity.getClass().getSimpleName());
            }

            // 2️⃣ Increment value
            Long nextVal = seq.getLastValue() + 1;
            seq.setLastValue(nextVal);

            // 3️⃣ Persist updated value using Hibernate mutation query
            session.createMutationQuery(
                            "UPDATE IdSequence s SET s.lastValue = :val WHERE s.entityName = :name")
                    .setParameter("val", nextVal)
                    .setParameter("name", entity.getClass().getSimpleName())
                    .executeUpdate();

            // 4️⃣ Return custom string ID
            return String.format("%s_%05d", prefix, nextVal);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HQL-based ID", e);
        }
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }
}
