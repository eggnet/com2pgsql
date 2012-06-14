  package extractor.filters;
  
  import java.io.PrintStream;
  import java.util.ArrayList;
  import java.util.List;
  
  public class FilterTextRemover
  {
    private String originalText = "";
    private boolean[] deletionMask;
    private List<TextCutPoint> cutPoints;
  
    public FilterTextRemover(String originalText)
    {
      this.originalText = originalText;
  
      this.deletionMask = new boolean[originalText.length()];
  
      for (int i = 0; i < this.deletionMask.length; i++) {
        this.deletionMask[i] = false;
      }
  
      this.cutPoints = new ArrayList();
    }
  
    public void markForDeletion(int start, int end) {
      this.cutPoints.add(new TextCutPoint(start, end));
      if ((start >= 0) && (end <= this.deletionMask.length)) {
        for (int i = start; i < end; i++)
          this.deletionMask[i] = true;
      }
      else {
        System.err.println("Warning! Trying to Delete out of Bounds: " + start + " until " + end + " but bounds are 0:" + this.deletionMask.length);
        System.err.println("Will not mark for deletion!");
      }
    }
  
    public String doDelete()
    {
      StringBuilder myStringBuilder = new StringBuilder();
  
      for (int i = 0; i < this.originalText.length(); i++) {
        if (this.deletionMask[i] == false)
          myStringBuilder.append(this.originalText.charAt(i));
      }
      return myStringBuilder.toString();
    }
  
    public String getText() {
      return this.originalText;
    }
  
    public class TextCutPoint
    {
      private int start;
      private int end;
  
      public int getStart()
      {
        return this.start;
      }
      public void setStart(int start) {
        this.start = start;
      }
      public int getEnd() {
        return this.end;
      }
      public void setEnd(int end) {
        this.end = end;
      }
  
      public TextCutPoint(int start, int end)
      {
        this.start = start;
        this.end = end;
      }
    }
  }

 