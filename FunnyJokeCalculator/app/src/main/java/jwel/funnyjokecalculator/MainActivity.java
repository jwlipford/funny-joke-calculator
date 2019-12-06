package jwel.funnyjokecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends AppCompatActivity
{
    private EditText  number1;
    private TextView  operator;
    private EditText  number2;
    
    private View        output;
    private TableLayout layout;
    private MediaPlayer mediaPlayer;
    
    private Jokes jokes;
    
    public final ContextThemeWrapper outputText_contentThemeWrapper
        = new ContextThemeWrapper( this, R.style.outputTextStyle );
    
    private double num1Val; // 1st number's value
    private double num2Val; // 2nd number's value
    private double rsltVal; // result value
    private String num1Str; // 1st number's String
    private String num2Str; // 2nd number's String
    private String rsltStr; // result String
    
    public double  getNum1Val(){ return num1Val; }
    public double  getNum2Val(){ return num2Val; }
    public double  getRsltVal(){ return rsltVal; }
    public String  getNum1Str(){ return num1Str; }
    public String  getNum2Str(){ return num2Str; }
    public String  getRsltStr(){ return rsltStr; }
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        
        this.number1  = super.findViewById( R.id.number1 );
        this.operator = super.findViewById( R.id.operator );
        this.number2  = super.findViewById( R.id.number2 );
        
        this.output = null;
        this.layout = super.findViewById( R.id.layout );
        
        this.jokes = new Jokes( this );
        
        Button buttonPlus   = super.findViewById( R.id.button_plus );
        Button buttonMinus  = super.findViewById( R.id.button_minus );
        Button buttonTimes  = super.findViewById( R.id.button_times );
        Button buttonDivide = super.findViewById( R.id.button_divide );
        Button buttonPower  = super.findViewById( R.id.button_power );
        Button buttonRoot   = super.findViewById( R.id.button_root );
        Button buttonLog    = super.findViewById( R.id.button_log );
        Button buttonMod    = super.findViewById( R.id.button_mod );
        
        buttonPlus.setOnClickListener(
            new OperatorListener( "+" ){
                @Override double findResult( double d1, double d2 )
                    { return d1 + d2; }
                @Override void lookForOperatorSpecificJokes()
                    { jokes.lookForAdditionJokes(); } } );
        buttonMinus.setOnClickListener(
            new OperatorListener( super.getString( R.string.minus ) ){
                @Override double findResult( double d1, double d2 )
                    { return d1 - d2; }
                @Override void lookForOperatorSpecificJokes()
                    { jokes.lookForSubtractionJokes(); } } );
        buttonTimes.setOnClickListener(
            new OperatorListener( super.getString( R.string.times ) ){
                @Override double findResult( double d1, double d2 )
                    { return d1 * d2; }
                @Override void lookForOperatorSpecificJokes()
                    { jokes.lookForMultiplicationJokes(); } } );
        buttonDivide.setOnClickListener(
            new OperatorListener( super.getString( R.string.divide ) ){
                @Override double findResult( double d1, double d2 )
                    { return d1 / d2; }
                @Override void lookForOperatorSpecificJokes()
                    { jokes.lookForDivisionJokes(); } } );
        buttonPower.setOnClickListener(
            new OperatorListener( "^" ){
                @Override double findResult( double d1, double d2 )
                    { return Math.pow( d1, d2 ); }
                @Override void lookForOperatorSpecificJokes()
                    { jokes.lookForPowerJokes(); } } );
        buttonRoot.setOnClickListener(
            new OperatorListener( super.getString( R.string.root ) ){
                @Override double findResult( double d1, double d2 )
                    { return Math.pow( d2, 1/d1 ); } // d1 root of d2
                @Override void lookForOperatorSpecificJokes()
                    { jokes.lookForRootJokes(); } } );
        buttonLog.setOnClickListener(
            new OperatorListener( super.getString( R.string.log ) ){
                @Override double findResult( double d1, double d2 )
                    // Java does not have Math.logBase(double,double), so we
                    // use the following formula:
                    { return Math.log(d2) / Math.log(d1); } // log base d1 of d2
                @Override void lookForOperatorSpecificJokes()
                    { jokes.lookForLogarithmJokes(); } } );
        buttonMod.setOnClickListener(
            new OperatorListener( super.getString( R.string.mod ) ) {
                @Override double findResult( double d1, double d2 )
                    { return d1 % d2; }
                @Override void lookForOperatorSpecificJokes()
                    { jokes.lookForModulusJokes(); } } );
    }
    
    public boolean numbersAre( double num1, double num2 )
    {
        return (this.num1Val == num1 && this.num2Val == num2);
    }
    
    public String getOutputText()
    // If output is a TextView, returns its text; otherwise, returns null
    {
        if( this.output instanceof TextView )
            return ((TextView)this.output).getText().toString();
        else
            return null;
    }
    
    public void parseNumbers()
    // Uses the text in number1 and number2 to set num1Str, num2Str, num1Val,
    // and num2Val. This method does NOT use exception handling on the 2 calls
    // of Double.parseDouble because inputType restrictions on number1 and
    // number2 prevent the user from entering bad Strings. This method does
    // set num1Val and num2Val to NaN if the corresponding Strings are empty.
    {
        this.num1Str = MainActivity.this.number1.getText().toString();
        this.num2Str = MainActivity.this.number2.getText().toString();
        this.num1Val = this.num1Str.isEmpty() ?
                       Double.NaN : Double.parseDouble( this.num1Str );
        this.num2Val = this.num2Str.isEmpty() ?
                       Double.NaN : Double.parseDouble( this.num2Str );
    }
    
    protected static String formatForOutput( double d, Integer maxDecimalPlaces )
    // Returns the conversion of d into a String in the format preferred for
    // this class's output View. Returns "Not a number" if d is NaN; converts
    // d to an integer (removing ".0") if possible; rounds to maxDecimalPlaces
    // decimal places if needed
    {
        if( Double.isNaN(d) )
            return "Not a number";
        else if( d == (int)d )
            return Integer.toString( (int)d );
        else if( maxDecimalPlaces != null )
            return String.format( "%." + maxDecimalPlaces + "f", d );
        else
            return Double.toString(d);
    }
    
    protected static String formatForOutput( double d )
    // Returns the conversion of d into a String in the format preferred for
    // this class's output View. Returns "Not a number" if d is NaN; converts
    // d to an integer (removing ".0") if possible.
    {
        return formatForOutput( d, null );
    }
    
    protected void setResult( double rsltVal )
    // Sets rsltVal and rsltStr. Uses formatForOutput method for rsltStr, and
    // inserts "= " at start unless result is NaN.
    {
        this.rsltVal = rsltVal;
        this.rsltStr = MainActivity.formatForOutput( rsltVal );
        if( !Double.isNaN( rsltVal ) )
            this.rsltStr = "= " + this.rsltStr;
    }
    
    public void setOutput( View view )
    // Displays view parameter as output under the rows of Buttons. Also
    // clears any animations and stops any running audio files.
    {
        if( this.output != null )
        {
            this.output.clearAnimation(); // If any
            this.layout.removeView( this.output );
        }
        if( this.mediaPlayer != null )
            this.mediaPlayer.stop();
        this.output = view;
        this.layout.addView( this.output );
    }
    
    public void setOutputAsTextView( CharSequence text )
    // Displays text parameter in a normal TextView, using outputTextStyle style
    {
        TextView output = new TextView( this.outputText_contentThemeWrapper );
        output.setText( text );
        this.setOutput( output );
    }
    
    public void setMediaPlayer( MediaPlayer mediaPlayer )
    {
        this.mediaPlayer = mediaPlayer;
    }
    
    
    
    protected abstract class OperatorListener implements OnClickListener
    // Each of the 8 Buttons represents a binary operator and has an
    // OperatorListener. onClick parses number1 and number2, finds the result
    // (depending on the operations) sets the operator text, and sets
    // the output, finding a joke if possible.
    {
        final MainActivity MAIN = MainActivity.this;
        String operatorStr;
        abstract double findResult( double d1, double d2 );
        abstract void lookForOperatorSpecificJokes();
        
        public OperatorListener( String operatorStr )
        {
            this.operatorStr = operatorStr;
        }
        
        @Override
        public void onClick( View view )
        {
            MAIN.parseNumbers();
            MAIN.setResult( this.findResult( MAIN.num1Val, MAIN.num2Val ) );
            MAIN.operator.setText( this.operatorStr );
            
            View oldOutput = MAIN.output;
            this.lookForOperatorSpecificJokes();
            if( MAIN.output == oldOutput )
            {
                MAIN.jokes.lookForResultJokes();
                if( MAIN.output == oldOutput )
                    MAIN.setOutputAsTextView( MAIN.rsltStr );
            }
        }
    }
}
