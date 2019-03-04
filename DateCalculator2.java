
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.Assert;
import org.junit.Test;

public class DateCalculator2 {
    private static int[] daysInMonthNormalYear = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    private static int[] daysInMonthLeapYear = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    private static int flagYear = 1901;

    public static void main(String args[]) {
        int result = getElapsedDays(args);
        System.out.println(result + " full days elapsed in between " + args[0] + " and " + args[1]);
    }

    private static int getElapsedDays(String[] args) {
        if (args.length != 2 || !isValueValid(args[0]) || !isValueValid(args[1])) {
            System.out.println("Incorrect input format. Input should be startDate, endDate in format yyyy-MM-dd between 1901-01-01 and 2999-12-31");
            return -1;
        }

        // Format already checked. try-catch not required to parse
        int year1 = Integer.parseInt(args[0].substring(0, 4));
        int month1 = Integer.parseInt(args[0].substring(5, 7));
        int date1 = Integer.parseInt(args[0].substring(8, 10));
        int year2 = Integer.parseInt(args[1].substring(0, 4));
        int month2 = Integer.parseInt(args[1].substring(5, 7));
        int date2 = Integer.parseInt(args[1].substring(8, 10));

        int delta = Math.abs(daysAfterFlagDate(year1, month1, date1) - daysAfterFlagDate(year2, month2, date2));
        int fullElapsed = delta > 0 ? delta - 1 : delta;
        return fullElapsed;
    }

    private static boolean isValueValid(String input) {
        if (!isFormatValid(input))
            return false;
        int year = Integer.parseInt(input.substring(0, 4));
        return year >= 1901 && year <= 2999;
    }

    // This regexp is hard to understand
    private static boolean isFormatValid(String input) {
        return input.matches(
                "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
    }

    private static int daysAfterFlagDate(int year, int month, int date) {
        int yearsAfterFlag = year - flagYear;
        int wholeYearDaysAfter = (yearsAfterFlag) * 365 + yearsAfterFlag / 4 - yearsAfterFlag / 100 + (yearsAfterFlag + 300) / 400;
        int daysOfTheYear = daysOfTheYear(year, month, date);
        return wholeYearDaysAfter + daysOfTheYear - 1;
    }

    private static int daysOfTheYear(int year, int month, int date) {
        int[] daysInMonth = (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) ? daysInMonthLeapYear : daysInMonthNormalYear;
        int output = 0;
        for (int i = 0; i < month - 1; i++)
            output += daysInMonth[i];
        return output + date;
    }

    @Test
    public void testIsFormatValid() {
        Assert.assertTrue(isFormatValid("1111-11-11"));
        Assert.assertFalse(isFormatValid("1111-1-11"));
        Assert.assertFalse(isFormatValid("111-11-11"));
        Assert.assertFalse(isFormatValid("aaaa-aa-aa"));
        Assert.assertFalse(isFormatValid("1111.11.11"));
        Assert.assertFalse(isFormatValid("1111111111"));
        Assert.assertFalse(isFormatValid("111a-11-11"));

        Assert.assertFalse(isFormatValid("1111-11-31"));
        Assert.assertFalse(isFormatValid("1111-00-31"));
        Assert.assertFalse(isFormatValid("2000-13-31"));
        Assert.assertTrue(isFormatValid("2000-02-29"));
        Assert.assertTrue(isFormatValid("2004-02-29"));
        Assert.assertFalse(isFormatValid("2100-02-29"));
    }

    @Test
    public void testDaysOfTheYearAndDaysAfterFlagDate() {
        LocalDate flagDate = LocalDate.of(1901, 1, 1);
        LocalDate d = flagDate;
        do {
            Assert.assertEquals(d.getDayOfYear(), daysOfTheYear(d.getYear(), d.getMonthValue(), d.getDayOfMonth()));
            if (ChronoUnit.DAYS.between(flagDate, d) == 36524) {
                System.out.println(d);
            }
            Assert.assertEquals(ChronoUnit.DAYS.between(flagDate, d), daysAfterFlagDate(d.getYear(), d.getMonthValue(), d.getDayOfMonth()));
            d = d.plusDays(1);
        } while (d.getYear() < 3000);
    }

    @Test
    public void testGetElapsedDays() {
        String[] testcase1 = { "1901-01-01", "1901-01-01" };
        Assert.assertEquals(0, getElapsedDays(testcase1));

        String[] testcase2 = { "1901-01-01", "2999-12-31" };
        LocalDate d1 = LocalDate.parse(testcase2[0]);
        LocalDate d2 = LocalDate.parse(testcase2[1]);
        Assert.assertEquals(ChronoUnit.DAYS.between(d1, d2) - 1, getElapsedDays(testcase2));

        String[] testcase3 = { "2999-12-30", "2999-12-31" };
        Assert.assertEquals(0, getElapsedDays(testcase3));

        String[] testcase4 = { "1999-12-30", "2000-12-31" };
        d1 = LocalDate.parse(testcase4[0]);
        d2 = LocalDate.parse(testcase4[1]);
        Assert.assertEquals(ChronoUnit.DAYS.between(d1, d2) - 1, getElapsedDays(testcase4));

        String[] testcase5 = { "1900-12-30", "2000-12-31" };
        Assert.assertEquals(-1, getElapsedDays(testcase5));

        String[] testcase6 = { "1999-12-30", "3000-12-31" };
        Assert.assertEquals(-1, getElapsedDays(testcase6));

        String[] testcase7 = { "1999.12.30", "1999.12.30" };
        Assert.assertEquals(-1, getElapsedDays(testcase7));

        String[] testcase8 = { "2999-12-28", "2999-12-31" };
        Assert.assertEquals(2, getElapsedDays(testcase8));
    }
}
