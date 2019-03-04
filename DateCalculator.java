import java.time.LocalDate;
import java.time.temporal.ChronoUnit;//These 2 imports are only for verifying the result. 

import org.junit.Assert;
import org.junit.Test;

public class DateCalculator {
    private static int[] daysInMonthNormalYear = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    private static int[] daysInMonthLeapYear = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    private static int flagYear = 1901;

    public static void main(String args[]) {
        int result = getElapsedDays(args);
        System.out.println(result + " full days elapsed in between " + args[0] + " and " + args[1]);
    }

    private static int getElapsedDays(String[] args) {
        // Firstly check the input format. If format is not correct, return -1
        if (args.length != 2 || !isFormatValid(args[0]) || !isFormatValid(args[1])) {
            System.out.println("Incorrect input format. Input should be startDate, endDate in format yyyy-MM-dd");
            return -1;
        }

        // Get year, month, date from the String. Format already checked. try-catch not
        // required to parse
        int year1 = Integer.parseInt(args[0].substring(0, 4));
        int month1 = Integer.parseInt(args[0].substring(5, 7));
        int date1 = Integer.parseInt(args[0].substring(8, 10));
        int year2 = Integer.parseInt(args[1].substring(0, 4));
        int month2 = Integer.parseInt(args[1].substring(5, 7));
        int date2 = Integer.parseInt(args[1].substring(8, 10));

        // Validate the value. Input should be a valid date between 1901-01-01 and
        // 2999-12-31 other wise return -1
        if (!isValueValid(year1, month1, date1) || !isValueValid(year2, month2, date2)) {
            System.out.println("Incorrect input value. Input should be a valid date between 1901-01-01 and 2999-12-31");
            return -1;
        }

        // The idea is to get the elapsed days since 1901-01-01 by the date1 and date2.
        // So the gap between elapsed1 and elapsed2 is the elapsed days between date1
        // and date2
        int delta = Math.abs(daysAfterFlagDate(year1, month1, date1) - daysAfterFlagDate(year2, month2, date2));

        // As per requirement only whole days elapsed are counted so elapsed-1 is the
        // expected result.
        // Two same day or two connected days are considered 0 elapsed
        int fullElapsed = delta - 1 > 0 ? delta - 1 : 0;
        return fullElapsed;
    }

    private static boolean isValueValid(int year, int month, int date) {
        return isYearValid(year) && isMonthValid(month) && isDateValid(year, month, date);
    }

    private static boolean isFormatValid(String input) {
        return input.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private static boolean isYearValid(int year) {
        return year >= 1901 && year <= 2999;
    }

    private static boolean isMonthValid(int month) {
        return month >= 1 && month <= 12;
    }

    private static boolean isDateValid(int year, int month, int date) {
        int maxDate = isLeap(year) ? daysInMonthLeapYear[month - 1] : daysInMonthNormalYear[month - 1];
        return date >= 1 && date <= maxDate;
    }

    private static boolean isLeap(int year) {
        return (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0));
    }

    // The logic to get days after the flag date (1901-01-01) is: Days by whole
    // years elapsed + days of the current year
    private static int daysAfterFlagDate(int year, int month, int date) {
        int yearsAfterFlag = year - flagYear;
        // The logic to get whole year days after 1901-01-01 is:
        // From 1901, Every full year count 365.
        // Every 4 years add 1 more (leap year).
        // Every 100 years minus 1(year is divisible by 100). From 2001,
        // Every 400 hears add 1 more back (year is divisible by 400)
        int wholeYearDaysAfter = (yearsAfterFlag) * 365 + yearsAfterFlag / 4 - yearsAfterFlag / 100 + (yearsAfterFlag + 300) / 400;
        int daysOfTheYear = daysOfTheYear(year, month, date);
        return wholeYearDaysAfter + daysOfTheYear - 1;// Need to minus 1
    }

    private static int daysOfTheYear(int year, int month, int date) {
        int[] daysInMonth = isLeap(year) ? daysInMonthLeapYear : daysInMonthNormalYear;
        int output = 0;
        for (int i = 0; i < month - 1; i++)
            output += daysInMonth[i];
        return output + date;
    }

    @Test
    public void testIsFormatValid() {
        Assert.assertTrue(isFormatValid("1111-11-11"));
        Assert.assertFalse(isFormatValid("111-11-11"));
        Assert.assertFalse(isFormatValid("aaaa-aa-aa"));
        Assert.assertFalse(isFormatValid("1111.11.11"));
        Assert.assertFalse(isFormatValid("1111111111"));
        Assert.assertFalse(isFormatValid("111a-11-11"));
    }

    @Test
    public void testIsYearValid() {
        for (int i = 1901; i <= 2999; i++)
            Assert.assertTrue(isYearValid(i));
        Assert.assertFalse(isYearValid(1900));
        Assert.assertFalse(isYearValid(3000));
    }

    @Test
    public void testIsMonthValid() {
        for (int i = 1; i <= 12; i++)
            Assert.assertTrue(isMonthValid(i));
        Assert.assertFalse(isMonthValid(0));
        Assert.assertFalse(isMonthValid(13));
    }

    @Test
    public void testIsDateValid() {
        LocalDate d = LocalDate.of(1901, 1, 1);
        do {
            Assert.assertTrue(isDateValid(d.getYear(), d.getMonthValue(), d.getDayOfMonth()));
            d = d.plusDays(1);
        } while (d.getYear() < 3000);
        Assert.assertTrue(isDateValid(2000, 2, 29));
        Assert.assertFalse(isDateValid(2000, 2, 30));
        Assert.assertFalse(isDateValid(1999, 2, 29));
        Assert.assertFalse(isDateValid(1900, 2, 29));
        Assert.assertFalse(isDateValid(2999, 12, 32));
        Assert.assertFalse(isDateValid(1999, 12, -1));
        Assert.assertFalse(isDateValid(1999, 12, 0));
    }

    @Test
    public void testIsLeap() {
        LocalDate d = LocalDate.of(1901, 1, 1);
        do {
            boolean isLeapYear = isLeap(d.getYear());
            Assert.assertEquals(d.isLeapYear(), isLeapYear);
            d = d.plusYears(1);
        } while (d.getYear() <= 2999);
    }

    @Test
    public void testIsValueValid() {
        Assert.assertTrue(isValueValid(1999, 9, 9));
        Assert.assertTrue(isValueValid(2000, 2, 29));
        Assert.assertFalse(isValueValid(1999, 19, 9));
        Assert.assertFalse(isValueValid(2100, 2, 29));
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
