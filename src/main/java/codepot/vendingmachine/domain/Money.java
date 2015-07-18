package codepot.vendingmachine.domain;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * immutable
 */
public class Money {

    private final BigDecimal value;

    public Money(double value) {
        this.value = round2(value);
    }

    public Money(BigDecimal value) {
        checkArgument(value != null);
        this.value = round2(value);
    }

    public Money add(Money that) {
        return new Money(this.value.add(that.value));
    }

    static Money money(double value){
        return new Money(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public boolean isZero() {
        return this.equals(money(0));
    }

    /**
     * @return not null
     */
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }

        if (object instanceof Money) {
            return hasSameValue((Money) object);
        }

        return false;
    }

    private boolean hasSameValue(Money that) {
        return value.compareTo(that.value) == 0;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static BigDecimal round2(double value) {
        return round2(new BigDecimal(value));
    }

    public static BigDecimal round2(BigDecimal value) {
        return value.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public Money substract(Money productPrice) {
        return new Money(value.subtract(productPrice.value));
    }

    public boolean greaterOrEquals(Money money) {
        return value.compareTo(money.value) > -1;
    }
}
