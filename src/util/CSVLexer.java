package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

public class CSVLexer
{
  public static final int YYEOF = -1;
  private static final int ZZ_BUFFERSIZE = 16384;
  public static final int BEFORE = 1;
  public static final int YYINITIAL = 0;
  public static final int COMMENT = 3;
  public static final int AFTER = 2;
  private static final String ZZ_CMAP_PACKED = "";
  private static final char[] ZZ_CMAP = zzUnpackCMap("");
  private static final int[] ZZ_ACTION = zzUnpackAction();
  private static final String ZZ_ACTION_PACKED_0 = "";
  private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
  private static final String ZZ_ROWMAP_PACKED_0 = "";
  private static final int[] ZZ_TRANS = zzUnpackTrans();
  private static final String ZZ_TRANS_PACKED_0 = "";
  private char[] zzcmap_instance = ZZ_CMAP;
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;
  private static final String[] ZZ_ERROR_MSG = { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
  private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
  private static final String ZZ_ATTRIBUTE_PACKED_0 = "";
  private Reader zzReader;
  private int zzState;
  private int zzLexicalState = 0;
  private char[] zzBuffer = new char[16384];
  private int zzMarkedPos;
  private int zzPushbackPos;
  private int zzCurrentPos;
  private int zzStartRead;
  private int zzEndRead;
  private int yyline;
  private int yychar;
  private int yycolumn;
  private boolean zzAtBOL = true;
  private boolean zzAtEOF;
  private char delimiter = ',';
  private char quote = '"';
  private String escapes = "";
  private String replacements = "";
  private String commentDelims = "";
  private int addLine = 1;
  private int lines = 0;

  private static int[] zzUnpackAction()
  {
    int[] arrayOfInt = new int[27];
    int i = 0;
    i = zzUnpackAction("", i, arrayOfInt);
    return arrayOfInt;
  }

  private static int zzUnpackAction(String paramString, int paramInt, int[] paramArrayOfInt)
  {
    int i = 0;
    int j = paramInt;
    int k = paramString.length();
    while (i < k)
    {
      int m = paramString.charAt(i++);
      int n = paramString.charAt(i++);
      do
      {
        paramArrayOfInt[(j++)] = n;
        m--;
      }
      while (m > 0);
    }
    return j;
  }

  private static int[] zzUnpackRowMap()
  {
    int[] arrayOfInt = new int[27];
    int i = 0;
    i = zzUnpackRowMap("", i, arrayOfInt);
    return arrayOfInt;
  }

  private static int zzUnpackRowMap(String paramString, int paramInt, int[] paramArrayOfInt)
  {
    int i = 0;
    int j = paramInt;
    int k = paramString.length();
    while (i < k)
    {
      int m = paramString.charAt(i++) << '\020';
      paramArrayOfInt[(j++)] = (m | paramString.charAt(i++));
    }
    return j;
  }

  private static int[] zzUnpackTrans()
  {
    int[] arrayOfInt = new int[126];
    int i = 0;
    i = zzUnpackTrans("", i, arrayOfInt);
    return arrayOfInt;
  }

  private static int zzUnpackTrans(String paramString, int paramInt, int[] paramArrayOfInt)
  {
    int i = 0;
    int j = paramInt;
    int k = paramString.length();
    while (i < k)
    {
      int m = paramString.charAt(i++);
      int n = paramString.charAt(i++);
      n--;
      do
      {
        paramArrayOfInt[(j++)] = n;
        m--;
      }
      while (m > 0);
    }
    return j;
  }

  private static int[] zzUnpackAttribute()
  {
    int[] arrayOfInt = new int[27];
    int i = 0;
    i = zzUnpackAttribute("", i, arrayOfInt);
    return arrayOfInt;
  }

  private static int zzUnpackAttribute(String paramString, int paramInt, int[] paramArrayOfInt)
  {
    int i = 0;
    int j = paramInt;
    int k = paramString.length();
    while (i < k)
    {
      int m = paramString.charAt(i++);
      int n = paramString.charAt(i++);
      do
      {
        paramArrayOfInt[(j++)] = n;
        m--;
      }
      while (m > 0);
    }
    return j;
  }

  public static void main(String[] paramArrayOfString)
  {
    try
    {
      Object localObject1;
      if (paramArrayOfString.length > 0)
      {
        File localObject2 = new File(paramArrayOfString[0]);
        if ((localObject2).exists())
        {
          if ((localObject2).canRead())
            localObject1 = new FileInputStream(localObject2);
          else
            throw new IOException("Could not open " + paramArrayOfString[0]);
        }
        else
          throw new IOException("Could not find " + paramArrayOfString[0]);
      }
      else
      {
        localObject1 = System.in;
      }
      Object localObject2 = new CSVLexer((InputStream)localObject1);
      ((CSVLexer)localObject2).setCommentStart("#;!");
      ((CSVLexer)localObject2).setEscapes("nrtf", "\n\r\t\f");
      String str;
      while ((str = ((CSVLexer)localObject2).getNextToken()) != null)
        System.out.println("" + ((CSVLexer)localObject2).getLineNumber() + " " + str);
    }
    catch (IOException localIOException)
    {
      System.out.println(localIOException.getMessage());
    }
  }

  private void ensureCharacterMapIsInstance()
  {
    if (ZZ_CMAP == this.zzcmap_instance)
    {
      this.zzcmap_instance = new char[ZZ_CMAP.length];
      System.arraycopy(ZZ_CMAP, 0, this.zzcmap_instance, 0, ZZ_CMAP.length);
    }
  }

  private boolean charIsSafe(char paramChar)
  {
    return (this.zzcmap_instance[paramChar] == ZZ_CMAP[97]) || (this.zzcmap_instance[paramChar] == ZZ_CMAP[9]);
  }

  private void updateCharacterClasses(char paramChar1, char paramChar2)
  {
    ensureCharacterMapIsInstance();
    this.zzcmap_instance[paramChar2] = this.zzcmap_instance[paramChar1];
    switch (paramChar1)
    {
    case '"':
    case ',':
      this.zzcmap_instance[paramChar1] = ZZ_CMAP[97];
      break;
    default:
      this.zzcmap_instance[paramChar1] = ZZ_CMAP[paramChar1];
    }
  }

  public void changeDelimiter(char paramChar)
    throws BadDelimiterException
  {
    if (paramChar == this.delimiter)
      return;
    if (!charIsSafe(paramChar))
      throw new BadDelimiterException(paramChar + " is not a safe delimiter.");
    updateCharacterClasses(this.delimiter, paramChar);
    this.delimiter = paramChar;
  }

  public void changeQuote(char paramChar)
    throws BadQuoteException
  {
    if (paramChar == this.quote)
      return;
    if (!charIsSafe(paramChar))
      throw new BadQuoteException(paramChar + " is not a safe quote.");
    updateCharacterClasses(this.quote, paramChar);
    this.quote = paramChar;
  }

  public void setEscapes(String paramString1, String paramString2)
  {
    int i = paramString1.length();
    if (paramString2.length() < i)
      i = paramString2.length();
    this.escapes = paramString1.substring(0, i);
    this.replacements = paramString2.substring(0, i);
  }

  private String unescape(String paramString)
  {
    if (paramString.indexOf('\\') == -1)
      return paramString.substring(1, paramString.length() - 1);
    StringBuffer localStringBuffer = new StringBuffer(paramString.length());
    for (int i = 1; i < paramString.length() - 1; i++)
    {
      char c1 = paramString.charAt(i);
      if (c1 == '\\')
      {
        i++;
        char c2 = paramString.charAt(i);
        if ((c2 == '\\') || (c2 == '"'))
        {
          localStringBuffer.append(c2);
        }
        else
        {
          int j;
          if ((j = this.escapes.indexOf(c2)) != -1)
            localStringBuffer.append(this.replacements.charAt(j));
          else
            localStringBuffer.append(c2);
        }
      }
      else
      {
        localStringBuffer.append(c1);
      }
    }
    return localStringBuffer.toString();
  }

  public void setCommentStart(String paramString)
  {
    this.commentDelims = paramString;
  }

  public int getLineNumber()
  {
    return this.lines;
  }

  public CSVLexer(Reader paramReader)
  {
    this.zzReader = paramReader;
  }

  public CSVLexer(InputStream paramInputStream)
  {
    this(new InputStreamReader(paramInputStream));
  }

  private static char[] zzUnpackCMap(String paramString)
  {
    char[] arrayOfChar = new char[65536];
    int i = 0;
    int j = 0;
    while (i < 30)
    {
      int k = paramString.charAt(i++);
      int m = paramString.charAt(i++);
      do
      {
        arrayOfChar[(j++)] = (char)m;
        k--;
      }
      while (k > 0);
    }
    return arrayOfChar;
  }

  private boolean zzRefill()
    throws IOException
  {
    if (this.zzStartRead > 0)
    {
      System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
      this.zzEndRead -= this.zzStartRead;
      this.zzCurrentPos -= this.zzStartRead;
      this.zzMarkedPos -= this.zzStartRead;
      this.zzPushbackPos -= this.zzStartRead;
      this.zzStartRead = 0;
    }
    if (this.zzCurrentPos >= this.zzBuffer.length)
    {
      char[] arrayOfChar = new char[this.zzCurrentPos * 2];
      System.arraycopy(this.zzBuffer, 0, arrayOfChar, 0, this.zzBuffer.length);
      this.zzBuffer = arrayOfChar;
    }
    int i = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
    if (i < 0)
      return true;
    this.zzEndRead += i;
    return false;
  }

  public final void yyclose()
    throws IOException
  {
    this.zzAtEOF = true;
    this.zzEndRead = this.zzStartRead;
    if (this.zzReader != null)
      this.zzReader.close();
  }

  public final void yyreset(Reader paramReader)
  {
    this.zzReader = paramReader;
    this.zzAtBOL = true;
    this.zzAtEOF = false;
    this.zzEndRead = (this.zzStartRead = 0);
    this.zzCurrentPos = (this.zzMarkedPos = this.zzPushbackPos = 0);
    this.yyline = (this.yychar = this.yycolumn = 0);
    this.zzLexicalState = 0;
  }

  public final int yystate()
  {
    return this.zzLexicalState;
  }

  public final void yybegin(int paramInt)
  {
    this.zzLexicalState = paramInt;
  }

  public final String yytext()
  {
    return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
  }

  public final char yycharat(int paramInt)
  {
    return this.zzBuffer[(this.zzStartRead + paramInt)];
  }

  public final int yylength()
  {
    return this.zzMarkedPos - this.zzStartRead;
  }

  private void zzScanError(int paramInt)
  {
    String str;
    try
    {
      str = ZZ_ERROR_MSG[paramInt];
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
      str = ZZ_ERROR_MSG[0];
    }
    throw new Error(str);
  }

  public void yypushback(int paramInt)
  {
    if (paramInt > yylength())
      zzScanError(2);
    this.zzMarkedPos -= paramInt;
  }

  public String getNextToken()
    throws IOException
  {
    int n = this.zzEndRead;
    char[] arrayOfChar1 = this.zzBuffer;
    char[] arrayOfChar2 = this.zzcmap_instance;
    int[] arrayOfInt1 = ZZ_TRANS;
    int[] arrayOfInt2 = ZZ_ROWMAP;
    int[] arrayOfInt3 = ZZ_ATTRIBUTE;
    while (true)
    {
      int m = this.zzMarkedPos;
      int j = -1;
      int k = this.zzCurrentPos = this.zzStartRead = m;
      this.zzState = this.zzLexicalState;
      int i;
      while (true)
      {
        if (k < n)
        {
          i = arrayOfChar1[(k++)];
        }
        else
        {
          if (this.zzAtEOF)
          {
            i = -1;
            break;
          }
          this.zzCurrentPos = k;
          this.zzMarkedPos = m;
          boolean bool = zzRefill();
          k = this.zzCurrentPos;
          m = this.zzMarkedPos;
          arrayOfChar1 = this.zzBuffer;
          n = this.zzEndRead;
          if (bool)
          {
            i = -1;
            break;
          }
          i = arrayOfChar1[(k++)];
        }
        int i1 = arrayOfInt1[(arrayOfInt2[this.zzState] + arrayOfChar2[i])];
        if (i1 == -1)
          break;
        this.zzState = i1;
        int i2 = arrayOfInt3[this.zzState];
        if ((i2 & 0x1) == 1)
        {
          j = this.zzState;
          m = k;
          if ((i2 & 0x8) == 8)
            break;
        }
      }
      this.zzMarkedPos = m;
      switch (j < 0 ? j : ZZ_ACTION[j])
      {
      case 2:
        this.lines += this.addLine;
        this.addLine = 0;
        String str = yytext();
        if (this.commentDelims.indexOf(str.charAt(0)) == -1)
        {
          yybegin(2);
          return str;
        }
        yybegin(3);
      case 14:
        break;
      case 8:
        this.addLine += 1;
        yybegin(0);
        return "";
      case 15:
        break;
      case 9:
        yybegin(1);
        return "";
      case 16:
        break;
      case 4:
        this.addLine += 1;
        yybegin(0);
      case 17:
        break;
      case 5:
        this.lines += this.addLine;
        this.addLine = 0;
        yybegin(1);
        return "";
      case 18:
        break;
      case 12:
        this.lines += this.addLine;
        this.addLine = 0;
        yybegin(2);
        return unescape(yytext());
      case 19:
        break;
      case 7:
        yybegin(2);
        return yytext();
      case 20:
        break;
      case 6:
        this.lines += this.addLine;
        this.addLine = 0;
        yybegin(0);
        return yytext();
      case 21:
        break;
      case 11:
        yybegin(1);
      case 22:
        break;
      case 13:
        yybegin(2);
        return unescape(yytext());
      case 23:
        break;
      case 10:
        yybegin(0);
        return yytext();
      case 24:
        break;
      case 1:
      case 25:
        break;
      case 3:
        this.lines += this.addLine;
        this.addLine = 0;
        yybegin(1);
      case 26:
        break;
      default:
        if ((i == -1) && (this.zzStartRead == this.zzCurrentPos))
          this.zzAtEOF = true;
        switch (this.zzLexicalState)
        {
        case 1:
          yybegin(0);
          this.addLine += 1;
          return "";
        case 28:
          break;
        default:
          return null;
        }
      }
    }
  }
}
