
package glinda;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {
    private boolean syntaxErrorFlag = true;
    String site_tittle = null;
    String exampleCode = "!head=\"my my website\""
            + "header code"
            + "!middle"
            + "!start "
            + "first news"
            + "!end"
            + "!start"
            + "second news"
            + "!end"
            + "!end";
    public Interpreter(String code){
        code = exampleCode;
        if(!code.contains("!head")){
            System.out.println("Syntax Error! !head command cannot found.");
        }else if(!code.contains("!middle")){
            System.out.println("Syntax Error! !middle command cannot found.");
        }else if(!code.contains("!end")){
            System.out.println("Syntax Error! !end command cannot found.");
        }else{
            syntaxErrorFlag = false;
            code = code.replaceAll("\n", "");
            code = code.replaceAll("\r", "");
            Pattern tittle_pattern = Pattern.compile("!head=\"(.*)\"");
            Matcher tittle_matcher = tittle_pattern.matcher(code);
            if(tittle_matcher.find()){
                site_tittle = tittle_matcher.group(1).trim();
                System.out.print("/ / / / ");
                for(int i = 0; i < site_tittle.length()/2 + 1; i++)
                    System.out.print("/ ");
                System.out.println("/ / / / ");
                System.out.println("/ / / / "+site_tittle+" / / / /");
                System.out.print("/ / / / ");
                for(int i = 0; i < site_tittle.length()/2 + 1; i++)
                    System.out.print("/ ");
                System.out.println("/ / / / ");
            }
            else
                syntaxErrorFlag = true;
            Pattern header_pattern = Pattern.compile("!head(.*)!middle");
            Matcher header_matcher = header_pattern.matcher(code);
            if(header_matcher.find() && !syntaxErrorFlag){
                String header_code = header_matcher.group(1).trim();
                // RESOLVE OTHER CODE ACCORDING TO THE VERSION 1.0
            }else
                syntaxErrorFlag = true;
            Pattern middle_pattern = Pattern.compile("!middle(.*)!end.");
            Matcher middle_matcher = middle_pattern.matcher(code);
            if(middle_matcher.find() && !syntaxErrorFlag){
                String middle_code = middle_matcher.group(1).trim();
                // RESOLVE OTHER CODE ACCORDING TO THE VERSION 1.0
                Pattern div_pattern = Pattern.compile("!start(.*)!end.");
                Matcher div_matcher = div_pattern.matcher(middle_code);
                String div = null;
                while(div_matcher.find() && !syntaxErrorFlag){
                    div = middle_matcher.group(1).trim();   // FIRST DIV
                    middle_code.replace(div, "");   // HANDLE ACCORDING TO THE SPECIFIED STYLES IN HEADER [SIMPLIFIED HERE
                    div = div.replace("!start", "");
                    div = div.replace("!end", "\n");    // SOME BUG FIXED
                    div = div.trim();
                    System.out.println(div+"\n");
                }
            }else
                syntaxErrorFlag = true;
        }
        if(syntaxErrorFlag){
            System.out.println("This web site doesn't include proper interpretable code!");
        }
    }
}
