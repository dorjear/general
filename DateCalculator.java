
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import org.junit.Assert;

public class DateCalculator {
    private static int[] daysInMonthNormalYear={31,28,31,30,31,30,31,31,30,31,30,31}; 
    private static int[] daysInMonthLeapYear={31,29,31,30,31,30,31,31,30,31,30,31};
    private static int flagYear = 1901;
    
    public static void main(String args[]) {
        int result = getElapsedDays(args);
        System.out.println(result + " full days elapsed in between "+args[0] + " and " + args[1]);
    }

    private static int getElapsedDays(String[] args) {
        if(args.length!=2 || !isFormatValid(args[0]) || !isFormatValid(args[1])) {
            System.out.println("Incorrect input format. Input should be startDate, endDate in format yyyy-MM-dd");
            return -1;
        }
        
        //Format already checked. try-catch not required to parse
        int year1 = Integer.parseInt(args[0].substring(0,4));
        int month1 = Integer.parseInt(args[0].substring(5,7));
        int date1 = Integer.parseInt(args[0].substring(8,10));
        int year2 = Integer.parseInt(args[1].substring(0,4));
        int month2 = Integer.parseInt(args[1].substring(5,7));
        int date2 = Integer.parseInt(args[1].substring(8,10));
        
        if(!isValueValid(year1, month1, date1) || !isValueValid(year2, month2, date2)) {
            System.out.println("Incorrect input value. Input should be a valid date between 1901-01-01 and 2999-12-31");
            return -1;
        }
        int delta = Math.abs(daysAfterFlagDate(year1, month1, date1) - daysAfterFlagDate(year2, month2, date2));
        int fullElapsed = delta>0?delta-1:delta;
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
        int maxDate = isLeap(year)?daysInMonthLeapYear[month-1]:daysInMonthNormalYear[month-1];
        return date >= 1 && date <= maxDate;
    }

    private static boolean isLeap(int year) {
        if(year % 400 ==0 || (year%4 ==0 && year%100!=0)) return true;
        else return false;
    }
    
    private static int daysAfterFlagDate(int year, int month, int date) {
        int yearsAfterFlag = year - flagYear;
        int wholeYearDaysAfter = (yearsAfterFlag)*365 + yearsAfterFlag/4 - yearsAfterFlag/100 + yearsAfterFlag/400;
        int daysOfTheYear = daysOfTheYear(year, month, date);
        return wholeYearDaysAfter + daysOfTheYear;
    }

    private static int daysOfTheYear(int year, int month, int date) {
        int[] daysInMonth = isLeap(year)?daysInMonthLeapYear:daysInMonthNormalYear;
        int output = 0;
        for(int i = 0; i < month-1; i++) output += daysInMonth[i];
        return output + date;
    }
    
    @Test
    public void testIsFormatValid(){
        Assert.assertTrue(isFormatValid("1111-11-11"));
        Assert.assertFalse(isFormatValid("111-11-11"));
        Assert.assertFalse(isFormatValid("aaaa-aa-aa"));
        Assert.assertFalse(isFormatValid("1111.11.11"));
        Assert.assertFalse(isFormatValid("1111111111"));
        Assert.assertFalse(isFormatValid("111a-11-11"));
    }

    @Test
    public void testIsYearValid(){
        for(int i = 1901; i<=2999; i++) Assert.assertTrue(isYearValid(i));
        Assert.assertFalse(isYearValid(1900));
        Assert.assertFalse(isYearValid(3000));
    }

    @Test
    public void testIsMonthValid(){
        for(int i = 1; i<=12; i++) Assert.assertTrue(isMonthValid(i));
        Assert.assertFalse(isMonthValid(0));
        Assert.assertFalse(isMonthValid(13));
    }

    @Test
    public void testIsDateValid(){
        LocalDate d = LocalDate.of(1901, 1, 1);
        do {
            Assert.assertTrue(isDateValid(d.getYear(), d.getMonthValue(), d.getDayOfMonth()));
            d = d.plusDays(1);
        }while(d.getYear()<3000);
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
        }while(d.getYear()<=2999);
    }
}
