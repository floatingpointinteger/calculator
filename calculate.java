import java.util.Scanner;

public class calculate
{
    public static double pow(double a, double b)
    {
        if(a == 0 && b == 0)
        {
            return Double.NaN; 
        }

        else
        {
            return Math.pow(a, b);
        }
    }

    public static double constructNum(String s)
    {
        double num = 0;
        double cof = 1;

        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) == '.')
            {
                cof = Math.pow(10, s.length() - i - 1);
                continue;
            }

            num = num * 10 + (s.charAt(i) - '0');
        }
        
        return num / cof;
    }

    public static double calcLeft(int idx, String s)
    {
        String subS = "";

        for(int i = 0; i < idx; i++)
        {
            subS = subS + s.charAt(i);
        }

        return parse(subS);
    }

    public static double calcRight(int idx, String s)
    {
        String subS = "";

        for(int i = idx + 1; i < s.length(); i++)
        {
            subS = subS + s.charAt(i);
        }

        return parse(subS);
    }

    public static String cleaner(String s)
    {
        String proposition = "";
        boolean flag = false;
        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) == '+' || s.charAt(i) == '-')
            {
                int temp = i;
                char ch = s.charAt(i);
                for(int j = i + 1; j < s.length() && (s.charAt(j) == '+' || s.charAt(j) == '-'); j++, i++)
                {
                    if(s.charAt(j) == '-' && ch == '-')
                    {
                        ch = '+';
                    }

                    else if(s.charAt(j) == '-' && ch == '+')
                    {
                        ch = '-';
                    }
                }

                if((temp - 1) > 0 && (s.charAt(temp - 1) == '*' || s.charAt(temp - 1) == '/' || s.charAt(temp - 1) == '^'))
                {
                    proposition = proposition + "(" + ch;
                    int brackets = 0;

                    for(int j = i + 1; j < s.length() && (brackets > 0 || (s.charAt(j) <= '9' && s.charAt(j) >= '0' || s.charAt(j) == '.' || s.charAt(j) == '('|| s.charAt(j) == ')')); j++, i++)
                    {
                        if(j != 0 && (s.charAt(j) == '+' || s.charAt(j) == '-'))
                        {
                            flag = true;
                        }
                        if(s.charAt(j) == '(')
                        {
                            brackets++;
                        }

                        else if(s.charAt(j) == ')')
                        {
                            brackets--;
                        }
                        proposition = proposition + s.charAt(j);
                    }

                    proposition = proposition + ")";
                }
                
                else
                {
                    proposition = proposition + ch;
                }
            }

            else
            {
                proposition = proposition + s.charAt(i);
            }
        }

        if(flag)
        {
            return cleaner(proposition);
        }

        return proposition;
    }

    public static double parse(String s)
    {
        char[] operators = {'+', '-', '*', '/', '^'};
        int opBias = operators.length - 2;
        int opIdx = -1;
        int braces = 0;

        if(s.charAt(0) == '-')
        {
            s = "0" + s;
        }

        if(s.charAt(0) == '+')
        {
            s = "0" + s;
        }


        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) == '(')
            {
                braces++;
            }

            else if(s.charAt(i) == ')')
            {
                braces--;
            }

            for(int j = 0; j <= opBias && braces == 0; j++)
            {
                if(s.charAt(i) == operators[j])
                {
                    opBias = j;
                    opIdx = i;
                }
            }
        }

        if(opIdx == -1)
        {
            braces = 0;
            opBias++;
            for(int i = s.length() - 1; i >= 0; i--)
            {
                if(s.charAt(i) == ')')
                {
                    braces++;
                }

                 else if(s.charAt(i) == '(')
                {
                    braces--;
                }
                
                if(braces == 0 && s.charAt(i) == operators[opBias])
                {
                    opIdx = i;
                }
            }
        }

        if(opIdx == -1 && !(s.charAt(0) == '('))
        {
            return constructNum(s);
        }

        else if(opIdx == -1)
        {
            String subS = "";

            for(int i = 1; i < s.length() - 1; i++)
            {
                subS = subS + s.charAt(i);
            }

            return parse(subS);
        }

        else if(opBias == 4)
        {
            return pow(calcLeft(opIdx, s), calcRight(opIdx, s));
        }

        else if(opBias == 3)
        {
            return calcLeft(opIdx, s) / calcRight(opIdx, s);
        }

        else if(opBias == 2)
        {
            return calcLeft(opIdx, s) * calcRight(opIdx, s);
        }

        else if(opBias == 1)
        {
            return calcLeft(opIdx, s) - calcRight(opIdx, s);
        }

        else
        {
            return calcLeft(opIdx, s) + calcRight(opIdx, s);
        }
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        String inpt = sc.nextLine();
        sc.close();

        String s = "";
        boolean flag = false;

        for(int i = 0; i < inpt.length(); i++)
        {
            if(inpt.charAt(i) != ' ')
            { 
                if(inpt.charAt(i) == '+' || inpt.charAt(i) == '-' || inpt.charAt(i) == '/' || inpt.charAt(i) == '*' || inpt.charAt(i) == '^' || inpt.charAt(i) == '.')
                {
                    flag = false;
                }
                
                else if(inpt.charAt(i) <= '9' && inpt.charAt(i) >= '0')
                {
                    if(flag)
                    {
                        System.out.println("#invalid inpt");
                        return;
                    }

                    if(i < inpt.length() - 1 && inpt.charAt(i + 1) == ' ')
                    {
                        flag = true;
                    }
                }

                s = s + inpt.charAt(i);
            }
        }

        if(s.length() == 0)
        {
            System.out.println("invalid inpt");
            return;
        }

        int braces = 0;
        boolean flagS = false;
        boolean flagP = false;
        char[] operators = {'*', '/', '^'};
        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) == '(')
            {
                braces++;
                flagS = true;

                if(i != 0 && s.charAt(i - 1) >= '0' && s.charAt(i - 1) <= '9')
                {
                    System.out.println("1invalid inpt");
                    return;
                }
            }

            else if(s.charAt(i) <= '9' && s.charAt(i) >= '0')
            {
                flagS = false;
            }

            else if(s.charAt(i) == ')')
            {
                if(flagS)
                {
                    System.out.println("2invalid inpt");
                    return;
                }

                if(i < s.length() - 1 && ((s.charAt(i + 1) >= '0' && s.charAt(i + 1) <= '9') || s.charAt(i + 1) == '('))
                {
                    System.out.println("3invalid inpt");
                    return;
                }

                braces--;
            }

            else if(i == s.length() - 1)
            {
                System.out.println("4invalid inpt");
                return;
            }

            else if(s.charAt(i) == '+' || s.charAt(i) == '-')
            {
                flagP = false;
                if(i < s.length() - 1 && s.charAt(i + 1) == ')')
                {
                    System.out.println("invalid inpt");
                    return;
                }
            }

            else if(s.charAt(i) == '.')
            {
                if(flagP)
                {
                    System.out.println("9invalid inpt");
                    return;
                }

                flagP = true;

                if(i == s.length() - 1)
                {
                    System.out.println("10invalid inpt");
                    return;
                }

                else if(!(s.charAt(i + 1) <= '9' && s.charAt(i + 1) >= '0'))
                {
                    System.out.println("11invalid inpt");
                    return;
                }
            }

            else
            {
                for(int j = 0; j < operators.length; j++)
                {
                    if(s.charAt(i) == operators[j])
                    {
                        if(i == 0)
                        {
                            System.out.println("5invalid inpt");
                            return;
                        }

                        else if(i >= s.length() - 1)
                        {
                            System.out.println("6invalid inpt");
                            return;
                        }

                        else if(!(s.charAt(i - 1) <= '9' && s.charAt(i - 1) >= '0' || s.charAt(i - 1) == ')'))
                        {
                            System.out.println("7invalid inpt");
                            return;
                        }

                        else if(!(s.charAt(i + 1) <= '9' && s.charAt(i + 1) >= '0' || s.charAt(i + 1) == '(' || s.charAt(i + 1) == '+' || s.charAt(i + 1) == '-' || s.charAt(i + 1) == '.'))
                        {
                            System.out.println("8invalid inpt");
                            return;
                        }
                        flagP = false;
                        break;
                    }

                    else if(j == operators.length - 1 && !(s.charAt(i) == '+' || s.charAt(i) == '-' || s.charAt(i) == '.'))
                    {
                        System.out.println("*invalid inpt");
                        return;
                    }
                }
            }

            if(braces < 0)
            {
                System.out.println("invalid braces");
                return;
            }

            if(i >= s.length() - 1 && braces > 0)
            {
                System.out.println("invalid closing braces");
                return;
            }
        }
        System.out.println(s);
        String proposition = cleaner(s);
        
        System.out.println(proposition);
        double result = parse(proposition);
        System.out.println(result);
    }
}