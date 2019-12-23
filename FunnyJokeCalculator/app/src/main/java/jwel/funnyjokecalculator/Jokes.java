package jwel.funnyjokecalculator;

import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.*;
import android.widget.*;
import java.util.Arrays;
import java.util.Random;

public class Jokes
{
    private MainActivity main;
    private Random random;
    
    public Jokes( MainActivity main )
    {
        this.main = main;
        this.random = new Random();
    }
    
    
    
    
    // Methods that look for jokes, and related fields:
    
    public void lookForAdditionJokes()
    {
        if( main.numbersAre( 2, 2 ) )
            main.setOutputAsTextView( "to + too = for" );
        else if( main.numbersAre( 9, 10 ) )
            execute9Plus10Joke();
        else if( suspiciouslyBinary( main.getNum1Str() ) && suspiciouslyBinary( main.getNum2Str() ) )
            executeBinaryJoke( false );
    }
    
    public void lookForSubtractionJokes()
    {
        if( main.numbersAre( 4, 1 ) ) // Big Shaq
            execute4Minus1Joke();
        else if( suspiciouslyBinary( main.getNum1Str() ) && suspiciouslyBinary( main.getNum2Str() ) )
            executeBinaryJoke( true );
        else if( (main.getNum2Val() == 1) && (main.getNum1Val() % 2 == 1) && (random.nextFloat() < 0.25) )
        // If user is subtracting 1 from an odd number, then with 1/4 chance do the following
        {
            main.setOutputAsTextView( main.getRsltStr() +
                "\n\nOdd numbers torment me, so I subtract 1 to get even" );
        }
    }
    
    public void lookForMultiplicationJokes()
    {
        if( main.numbersAre( 2, 2 ) )
            main.setOutputAsTextView( "to × too = for" );
        if( main.numbersAre( 6, 9 ) )
            this.execute6Times9Joke();
    }
    
    public void lookForDivisionJokes()
    {
        if( main.getNum2Val() == 0 )
            this.executeDivideBy0Joke();
    }
    
    public void lookForPowerJokes()
    {
        if( main.numbersAre( 2, 2 ) )
            main.setOutputAsTextView( "2^2 = 2×2 = 2+2 = 4\nIsn't that cool?" );
        if( main.numbersAre( -1, 0.5 ) )
            main.setOutputAsTextView( Html.fromHtml( "<i>i</i> am the answer", 0 ) );
            // "i am the answer", with 'i' italicized
    }
    
    public void lookForRootJokes()
    {
        if( main.numbersAre( 2, -1 ) )
            main.setOutputAsTextView( Html.fromHtml( "<i>i</i> am the answer", 0 ) );
            // "i am the answer", with 'i' italicized
        else if( main.numbersAre( 2, 4 ) )
            main.setOutputAsTextView( "√4 = 4÷2 = 4－2 = 2\nIsn't that cool?" );
    }
    
    public void lookForLogarithmJokes()
    {
        // Do nothing in this version of the program
    }
    
    public void lookForModulusJokes()
    {
        // Do nothing in this version of the program
    }
    
    // Mole = 6.022*10^23. (Access quickly with 20.9456^18.)
    private static final double MOLE_MIN = 6.021 * Math.pow( 10, 23 );
    private static final double MOLE_MAX = 6.023 * Math.pow( 10, 23 );
    
    private static final int[] HEX_WORDS = new int[]
    // Array must be pre-sorted for use in binary search.
    // 0xCAFEBABE and 0xDECAFBAD are negative (‭‭-889275714‬ and -557122643‬,
    // respectively), so they come first.
    {
        0xCAFEBABE, 0xDECAFBAD, 0xACE, 0xADD, 0xBAD, 0xBED, 0xBEE, 0xBABE,
        0xBEEF, 0xCAFE, 0xDEAD, 0xFACE, 0xDECAF
    };
    
    public void lookForResultJokes()
    {
        double result = main.getRsltVal();
        
        if( 3.14 <= result && result < 3.15 ) // pi
        {
            this.executePieJoke( main.getRsltStr(), 1 );
        }
        else if( 6.28 <= result && result < 6.29 ) // 2*pi
        {
            this.executePieJoke( main.getRsltStr(), 2 );
        }
        else if( 9.42 <= result && result <= 9.43 ) // 3*pi
        {
            this.executePieJoke( main.getRsltStr(), 3 );
        }
        else if( 2.71 <= result && result <= 2.72 ) // Euler's number
        {
            ImageView output = new ImageView( main );
            output.setImageResource( R.drawable.oil );
            main.setOutput( output );
        }
        else if( 1.61 <= result && result <= 1.62 ) // golden ratio
        {
            ImageView output = new ImageView( main );
            output.setImageResource( R.drawable.goldenratio );
            main.setOutput( output );
        }
        else if( result == 13 )
        {
            this.execute13Joke();
        }
        else if( result == 69 )
        {
            this.execute69Joke();
        }
        else if( result == 250 )
        {
            // "In Chinese slang, the number 250 means 'idiot'",
            // according to en.wikipedia.org/wiki/250_%28number%29
            main.setOutputAsTextView( "= 250\n这个答案太疯狂了。" );
        }
        else if( result == 420 )
        {
            this.execute420Joke();
        }
        else if( result == 505 )
        {
            this.executeMorseCodeSOSJoke();
        }
        else if( result == 666 )
        {
            this.main.finish(); // Close Activity
        }
        else if( result == 1134 )
        {
            this.executeHelloJoke();
        }
        else if( MOLE_MIN <= result && result < MOLE_MAX )
        {
            ImageView output = new ImageView( main );
            output.setImageResource( R.drawable.mole );
            main.setOutput( output );
        }
        else if( result == (int)result && Arrays.binarySearch( HEX_WORDS, (int)result ) >= 0 )
        // If HEX_WORDS contains result, then execute this joke.
        // (One example to test: 0xACE=2766)
        {
            main.setOutputAsTextView(
                "= " + Integer.toHexString( (int)result ).toUpperCase() + "ₕₑₓ" );
        }
        else if( result >= 256 && Math.log( result ) / Math.log(2) % 1 == 0 )
        // if result is a power of 2 not less than 2^8
        {
            main.setOutputAsTextView(
                main.getRsltStr() + "\nOh look, a power of 2! Yay!" );
        }
        else if( !Double.isNaN( result ) && random.nextFloat() < 1/12f )
        // If result is not NaN, then with a 1/12 chance, output another
        // expression equal to the result
        {
            main.setOutputAsTextView( this.randomlyComplicate( result ) + "  :)" );
        }
        else if( random.nextFloat() < 1/50f )
        // With a 1/50 chance, under result include a link to a YouTube video
        {
            this.executeRickRoll();
        }
    }
    
    
    
    
    // Methods that execute jokes, and related methods and fields:
    
    private void execute9Plus10Joke()
    {
        TextView equals21 = new TextView( main.outputText_contentThemeWrapper );
        equals21.setTextSize( TypedValue.COMPLEX_UNIT_SP, 90 );
        equals21.setText( "= 21" );
    
        TextView jk = new TextView( main.outputText_contentThemeWrapper );
        jk.setTextSize( TypedValue.COMPLEX_UNIT_SP, 20 );
        jk.setText( "(JK, it's actually 19)" );
    
        LinearLayout output = new LinearLayout( main );
        output.setOrientation( LinearLayout.VERTICAL );
        output.addView( equals21 );
        output.addView( jk );
    
        main.setOutput( output, true );
        main.setMediaPlayer( MediaPlayer.create( main, R.raw.twentyone ) );
        main.startMediaPlayer();
    }
    
    private void execute4Minus1Joke()
    {
        main.setOutputAsTextView(
            "2 + 2 = 4,     \n     4 − 1 = 3\nQuick math", true );
        main.setMediaPlayer( MediaPlayer.create( main, R.raw.quick_math ) );
        main.startMediaPlayer();
    }
    
    private void execute6Times9Joke()
    {
        TextView equals42 = new TextView( main.outputText_contentThemeWrapper );
        equals42.setTextSize( TypedValue.COMPLEX_UNIT_SP, 90 );
        equals42.setText( "= 42" );
    
        TextView adtsc = new TextView( main.outputText_contentThemeWrapper );
        adtsc.setTextSize( TypedValue.COMPLEX_UNIT_SP, 20 );
        adtsc.setText( "(according to the Deep Thought supercomputer)" );
    
        LinearLayout output = new LinearLayout( main );
        output.setOrientation( LinearLayout.VERTICAL );
        output.addView( equals42 );
        output.addView( adtsc );
    
        main.setOutput( output, true );
    }
    
    private static final String[] outputsFor13 = new String[]
        {
            "≈ 12.999999999",
            "= 12 + 1",
            "= 14 - 1",
            "= Dₕₑₓ",
            "is the 6th prime number",
            "is the 7th Fibonacci number",
            "Ahhhhhhhhhhhhh!",
            "Please stop calculating this number, it's scary!"
        };
    
    private int prev13JokeIndex = 7;
        // We do not want outputsFor13[7] picked first, so this starts at 7.
    
    private void execute13Joke()
    // Pick a random element of outputsFor13, not the same as previous
    {
        int index = random.nextInt( Jokes.outputsFor13.length - 1 );
        if( index == this.prev13JokeIndex )
            index = Jokes.outputsFor13.length - 1;
        main.setOutputAsTextView( outputsFor13[ index ] );
        this.prev13JokeIndex = index;
    }
    
    private int numTimesCalculated69 = 0;
    
    private void execute69Joke()
    {
        ++this.numTimesCalculated69;
        switch( this.numTimesCalculated69 )
        {
            case 1: main.setOutputAsTextView( "= 69" ); break;
            case 2: main.setOutputAsTextView( "NOPE" ); break;
            case 3: main.setOutputAsTextView( "Still NOPE" ); break;
            default: // >=4
                StringBuilder outputText = new StringBuilder( "Still " );
                // Append "still " (numTimesCalculated69 - 3) times, since first
                // 3 times we had 3 special cases above, unless this number
                // exceeds some max number of times, which we will set
                // as 68.
                int stillAppendTimes = Math.min( this.numTimesCalculated69 - 3, 68 );
                for( int i = 0; i < stillAppendTimes; ++i )
                    outputText.append( "still " );
                outputText.append( "NOPE" );
                main.setOutputAsTextView( outputText, true );
        }
    }
    
    private void execute420Joke()
    {
        ImageView weed = new ImageView( main );
        weed.setImageResource( R.drawable.weed );
        weed.setForegroundGravity( Gravity.CENTER );
        
        RotateAnimation ra = new RotateAnimation(
            -3, 723, // angles
            Animation.RELATIVE_TO_SELF, 0.5f, // pivot x
            Animation.RELATIVE_TO_SELF, 0.79f // pivot y
        );
        ra.setRepeatMode( Animation.REVERSE );
        ra.setRepeatCount( Animation.INFINITE );
        ra.setDuration( 2500 );
        ra.setInterpolator( new AccelerateDecelerateInterpolator() );
        
        AnimationSet set = new AnimationSet( true );
        set.addAnimation( ra );
        
        main.setOutput( weed );
        weed.startAnimation( set );
        main.setMediaPlayer( MediaPlayer.create( main, R.raw.swed ) );
        main.startMediaPlayer();
    }
    
    private static boolean suspiciouslyBinary( String s ) // Improve later?
    {
        return s.length() >= 8 && s.matches( "[10.\\-+]+" );
    }
    
    private void executeBinaryJoke( boolean subtract )
    // main.getNum1Str() and main.getNum2Str() should look like binary numbers
    // if this joke is executed. Parameter subtract indicates whether the
    // operation is subtraction (true) or addition (false).
    {
        String outputText = "What is this, Binary?\nOkay, here you go:\n";
        int bin1, bin2;
        try
        {
            bin1 = Integer.parseInt( main.getNum1Str(), 2 );
            bin2 = Integer.parseInt( main.getNum2Str(), 2 );
        }
        catch( Exception x )
        {
            return; // main's output View does not change
        }
        int binRslt = subtract ? (bin1 - bin2) : (bin1 + bin2);
        outputText += Integer.toBinaryString( binRslt );
        outputText += "\n(If you meant Decimal, the result is ";
        if( Double.isNaN( main.getRsltVal() ) )
            outputText += "not a number";
        else
            outputText += MainActivity.formatForOutput( main.getRsltVal() );
        outputText += ")";
        
        main.setOutputAsTextView( outputText, true );
    }
    
    private void executeDivideBy0Joke()
    {
        final String[] slicableFoods = new String[]{ "pizzas", "pies", "cakes" };
        String foods = slicableFoods[ random.nextInt( slicableFoods.length ) ];
        
        TextView output = new TextView( main.outputText_contentThemeWrapper );
        output.setTextSize( TypedValue.COMPLEX_UNIT_SP, 30 );
        output.setText(
            "What in FUDGE are you DOING???\nYou can't divide by 0. It makes no sense.\n" +
                "Imagine you have " + main.getNum1Str() + ' ' + foods.toUpperCase() +
                ". If you divide your " + foods + " into 0 equal slices, how big is each slice?\n" +
                "Can't answer, can you? That's because your question is DUMB."
        );
        main.setOutput( output, true );
    }
    
    private void executeHelloJoke()
    {
        final TextView output = new TextView( main.outputText_contentThemeWrapper );
        output.setTypeface( Typeface.MONOSPACE );
        output.setText( "= 01134  " );
        
        RotateAnimation upsideDownRotate =
            new RotateAnimation(
                0, 180, // angles
                Animation.RELATIVE_TO_SELF, 0.5f, // pivot x
                Animation.RELATIVE_TO_SELF, 0.5f  // pivot y
            );
        upsideDownRotate.setStartOffset( 600 );
        upsideDownRotate.setDuration( 1134 );
        upsideDownRotate.setAnimationListener( new Animation.AnimationListener()
        {
            @Override
            public void onAnimationEnd( Animation animation )
            {
                output.setText( "hEllO" );
            }
            
            @Override public void onAnimationStart( Animation animation ){}
            @Override public void onAnimationRepeat( Animation animation ){}
        } );
    
        main.setOutput( output );
        output.startAnimation( upsideDownRotate );
    }
    
    private void executeMorseCodeSOSJoke()
    {
        ImageView output = new ImageView( main );
        output.setBackgroundResource( R.drawable.animation_505_sos );
        AnimationDrawable anim = (AnimationDrawable)output.getBackground();
        main.setOutput( output );
        anim.start();
    }
    
    private void executePieJoke( String text, int numPies )
    {
        TextView textView =
            new TextView( main.outputText_contentThemeWrapper );
        textView.setText( text );
        
        TableRow pies = new TableRow( main );
        for( int i = 0; i < numPies; ++i )
        {
            ImageView pie = new ImageView( main );
            pie.setImageResource( R.drawable.pie );
            pies.addView( pie );
        }
        
        TableLayout output = new TableLayout( main );
        output.setShrinkAllColumns( true ); // Shrink all pies to fit on screen
        output.addView( textView );
        output.addView( pies );
        
        main.setOutput( output );
    }
    
    private String randomlyComplicate( double d )
    // Randomly creates a String expressing a new addition or subtraction
    // operation whose result is d.
    {
        if( Double.isNaN(d) )
            return MainActivity.formatForOutput(d);
        
        float f = (float)d;
        float r = (float)this.random.nextGaussian() * f + f;
            // "Gaussian" means standard normal distribution (bell curve)
        if( f == (int)f )
            r = (int)(r + 1); // round away from 0
        
        switch( this.random.nextInt(4) )
        {
            case 0: return "= " + MainActivity.formatForOutput( f - r, 4 )
                           + " + " + MainActivity.formatForOutput( r, 4 );
            case 1: return "= " + MainActivity.formatForOutput( r, 4 ) +
                           " + " + MainActivity.formatForOutput( f - r, 4 );
            case 2: return "= " + MainActivity.formatForOutput( f + r, 4 ) +
                           " － " + MainActivity.formatForOutput( r, 4 );
            case 3: return "= " + MainActivity.formatForOutput( r, 4 ) +
                           " － "+ MainActivity.formatForOutput( r - f, 4 );
            default: // should not happen
                    return "= " + MainActivity.formatForOutput(d);
        }
    }
    
    private void executeRickRoll()
    {
        TextView output = new TextView( main.outputText_contentThemeWrapper );
        output.setText( Html.fromHtml(
            main.getRsltStr() + "<br/><br/>" +
            "Tap <a href='https://youtu.be/dQw4w9WgXcQ'>here</a> " +
            "for a super-special, ultra-rare joke!", 0 ) );
        output.setMovementMethod( LinkMovementMethod.getInstance());
        
        main.setOutput( output, true );
    }
}


/*
Possible future improvements:
    <A positive number> * 0.15 or 1.15  =>  "Tipping the waiter, I see. I think they deserve <some big number> dollars!"
    Expand list of Hex words
    Improve suspiciouslyBinary method?
    999 joke?
    16 shots joke?
    Picture of a log for natural log?
*/
