public class GregorianDate extends Date {

    private static final int[] MONTH_LENGTHS = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    public GregorianDate(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
    }


    // YOUR CODE HERE

    @Override
    public Date nextDate() {
        if(month == 12 && dayOfMonth == 31) {
            return new GregorianDate(year+1, 1, 1);
        } else if (dayOfMonth == getMonthLength(month)) {
            return new GregorianDate(year, month+1, 1);
        } else {
            return new GregorianDate(year, month, dayOfMonth+1);
        }
    }

    @Override
    public int dayOfYear() {
        int precedingMonthDays = 0;
        for (int m = 1; m < month; m += 1) {
            precedingMonthDays += getMonthLength(m);
        }
        return precedingMonthDays + dayOfMonth;
    }

    private static int getMonthLength(int m) {
        return MONTH_LENGTHS[m - 1];
    }
}