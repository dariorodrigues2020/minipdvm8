package cordova.plugin.printer.minipdvm8;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// MODULOS SAT, Termica E ServicoE1 DA LIB_E1 ***
import com.elgin.e1.Fiscal.SAT;
import com.elgin.e1.Impressora.Termica;
import com.elgin.e1.Servico.ServicoE1;

import java.util.Random;

public class minipdvm8 extends CordovaPlugin {
    public boolean execute( String action, JSONArray args, CallbackContext callbackContext ) throws JSONException 
    {
        if ( action.equals( "printText" ) ) 
	{            
            this.printText( args.getString( 0 ), callbackContext );
            return true;
        }
		
	if ( action.equals( "printQRCode" ) ) 
	{            
            this.printQRCode( args.getString( 0 ), callbackContext );
            return true;
        }
		
        return false;
    }
	
    private void printText( String args, CallbackContext callbackContext ) throws JSONException
    {		
	JSONObject json = new JSONObject( args );
		
	String text      = json.getString( "text"       );
	String conection = json.getString( "conection"  );
	String model     = json.getString( "model"      );
		
	int type      = Integer.parseInt( json.getString( "type"      ) );
	int parameter = Integer.parseInt( json.getString( "parameter" ) );
	int position  = Integer.parseInt( json.getString( "position"  ) );
	int style     = Integer.parseInt( json.getString( "style"     ) );
	int size      = Integer.parseInt( json.getString( "size"      ) );
	int corte     = Integer.parseInt( json.getString( "corte"     ) );
	int avanco    = Integer.parseInt( json.getString( "avanco"    ) );
		
	String OK = 'ok';
		
        if ( text != null && text.length( ) > 0 ) 
	{
	    Thread t = new Thread( ) 
	    {
	        @Override
	        public void run( ) 
		{
		    Termica.ImpressaoTexto( text, position, style, size );	

		    if ( corte == 1 ) 
		    {
			Termica.Corte( avanco );
		    }
					
		    Termica.FechaConexaoImpressora( );				   
		}
	    };
			
	    try 
	    {
		Termica.setContext( cordova.getActivity( ) );				
		int result = Termica.AbreConexaoImpressora( type, model, conection, parameter );	
				
		if ( result == 0 )
		{
		    t.start( );						
		    callbackContext.success( OK );
		} else
		{
		    callbackContext.error( result );
		}
	    } catch ( Exception ex )
	    {
		ex.printStackTrace( );
		callbackContext.error( ex.getMessage( ) );
	    }		
        } else 
	{
            callbackContext.error( "Texto invalido para impressao." );
        }
    }
	
	private void printQRCode( String args, CallbackContext callbackContext ) throws JSONException
	{		
		JSONObject json = new JSONObject( args );
		
		String text      = json.getString( "text"       );
		String conection = json.getString( "conection"  );
		String model     = json.getString( "model"      );
		
		int type      = Integer.parseInt( json.getString( "type"      ) );
		int parameter = Integer.parseInt( json.getString( "parameter" ) );
		int nivel     = Integer.parseInt( json.getString( "nivel"     ) );
		int size      = Integer.parseInt( json.getString( "size"      ) );
		
		if ( text != null && text.length( ) > 0 ) 
		{
			Thread t = new Thread( ) 
			{
				@Override
				public void run( ) 
				{
				    
				}
			};
			
			try 
			{
				Termica.setContext( cordova.getActivity( ) );
		
				int result = Termica.AbreConexaoImpressora( type, model, conection, parameter );

				if ( result == 0 )
				{					
					int r = Termica.ImpressaoQRCode( text, size, nivel );
					Termica.FechaConexaoImpressora( );	

					callbackContext.success( r );	
				} else
				{
					callbackContext.success( result );
				}
				//t.start( );
			} catch ( Exception ex )
			{
				ex.printStackTrace( );
				callbackContext.error( ex.getMessage( ) );
			}		
        } else 
		{
            callbackContext.error( "Texto invalido para impressao." );
        }
    }
}
