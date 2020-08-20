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

import br.com.bematech.android.miniprinter.Alignment;
import br.com.bematech.android.miniprinter.BitmapConverter;
import br.com.bematech.android.miniprinter.FormattedText;
import br.com.bematech.android.miniprinter.LineFeed;
import br.com.bematech.android.miniprinter.LineThickness;
import br.com.bematech.android.miniprinter.NetworkPrinterInfo;
import br.com.bematech.android.miniprinter.PrintedBitmap;
import br.com.bematech.android.miniprinter.Printer;
import br.com.bematech.android.miniprinter.PrinterCommands;
import br.com.bematech.android.miniprinter.PrinterModel;
import br.com.bematech.android.miniprinter.PrinterStatus;
import br.com.bematech.android.miniprinter.Receipt;
import br.com.bematech.android.miniprinter.SearchPrinter;
import br.com.bematech.android.miniprinter.barcode.CODE128;
import br.com.bematech.android.miniprinter.barcode.CODE39;
import br.com.bematech.android.miniprinter.barcode.CODE93;
import br.com.bematech.android.miniprinter.barcode.CODEBAR;
import br.com.bematech.android.miniprinter.barcode.EAN13;
import br.com.bematech.android.miniprinter.barcode.EAN8;
import br.com.bematech.android.miniprinter.barcode.ISBN;
import br.com.bematech.android.miniprinter.barcode.ITF;
import br.com.bematech.android.miniprinter.barcode.MSI;
import br.com.bematech.android.miniprinter.barcode.PDF417;
import br.com.bematech.android.miniprinter.barcode.PLESSEY;
import br.com.bematech.android.miniprinter.barcode.QRCode;
import br.com.bematech.android.miniprinter.barcode.UPCA;
import br.com.bematech.android.miniprinter.barcode.UPCE;
import br.com.bematech.android.miniprinter.exception.ConnectionException;

public class minipdvm8 extends CordovaPlugin {
	public boolean execute( String action, JSONArray args, CallbackContext callbackContext ) throws JSONException 
	{
		if ( action.equals( "open" ) ) 
		{            
            this.open( args.getString( 0 ), callbackContext );
            return true;
        }
		
		if ( action.equals( "close" ) ) 
		{            
            this.close( args.getString( 0 ), callbackContext );
            return true;
        }
		
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
		
		if ( action.equals( "printTextBema" ) ) 
		{            
            this.printTextBema( args.getString( 0 ), callbackContext );
            return true;
        }
		
        return false;
    }
	
	private void printTextBema( String args, CallbackContext callbackContext ) throws JSONException
	{
		JSONObject json = new JSONObject( args );
		
		String text   = json.getString( "text"   );
		String text2  = json.getString( "text2"  );
		String qrcode = json.getString( "qrcode" );
		String ip     = json.getString( "ip"     );
		String mac    = json.getString( "mac"    );
		String OK     = "ok";
		
		if ( text != null && text.length( ) > 0 ) 
		{
			Thread t = new Thread( ) 
			{
				@Override
				public void run( ) 
				{
					/*Printer printer = new Printer( );
					
					printer.FindPrinter( );
					
					printer.ImprimirTexto( text );
					
					if ( qrcode != null && qrcode.length( ) > 0 ) 
					{
						printer.ImprimirQRCode( qrcode );
					}
					
					if ( text2 != null && text2.length( ) > 0 ) 
					{
						printer.ImprimirTexto( text2 );
					}
					
					printer.CortarTotal( );*/
				}
			};
			
			try 
			{				
				/*t.start( );		
					
				callbackContext.success( OK );*/
				
				Printer printer = new Printer( cordova.getActivity( ), new NetworkPrinterInfo( PrinterModel.MP4200TH, ip, 9100, mac ) );
				
				PrinterStatus status = printer.getStatus( );
				String msg = "Status Impressora: ";
				msg += status.name( );
				callbackContext.error( msg );
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
	
	private void open( String args, CallbackContext callbackContext ) throws JSONException
	{		
		JSONObject json = new JSONObject( args );
		
		String conection = json.getString( "conection"  );
		String model     = json.getString( "model"      );
		
		int type      = Integer.parseInt( json.getString( "type"      ) );
		int parameter = Integer.parseInt( json.getString( "parameter" ) );
		
		String OK = "ok";
		
		Termica.setContext( cordova.getActivity( ) );	
		
		int result = Termica.AbreConexaoImpressora( type, model, conection, parameter );	
				
		if ( result == 0 )
		{
			callbackContext.success( OK );
		} else
		{
			callbackContext.error( result );
		}
	}
	
	private void close( String args, CallbackContext callbackContext ) throws JSONException
	{
		JSONObject json = new JSONObject( args );

		int corte  = Integer.parseInt( json.getString( "corte"  ) );
		int avanco = Integer.parseInt( json.getString( "avanco" ) );		
		
		String OK = "ok";

		Termica.setContext( cordova.getActivity( ) );	
		
		if ( corte == 1 ) {
			Termica.Corte( avanco );
		} else if ( avanco > 0 ) 
		{
		   Termica.AvancaPapel( avanco );
		}
				
		int result = Termica.FechaConexaoImpressora( );	
		
		if ( result == 0 )
		{
			callbackContext.success( OK );
		} else
		{
			callbackContext.error( result );
		}
	}
	
	private void printText( String args, CallbackContext callbackContext ) throws JSONException
	{		
		JSONObject json = new JSONObject( args );
		
		String text   = json.getString( "text"   );
		String qrcode = json.getString( "qrcode" );
		
		int position = Integer.parseInt( json.getString( "position" ) );
		int style    = Integer.parseInt( json.getString( "style"    ) );
		int size     = Integer.parseInt( json.getString( "size"     ) );
		int nivel    = Integer.parseInt( json.getString( "nivel"    ) );
		int sizeCode = Integer.parseInt( json.getString( "sizeCode" ) );
		int fecha    = Integer.parseInt( json.getString( "fecha"    ) );
		int corte    = Integer.parseInt( json.getString( "corte"    ) );
		int avanco   = Integer.parseInt( json.getString( "avanco"   ) );
		
		String OK = "ok";
		
        if ( text != null && text.length( ) > 0 ) 
		{
			Thread t = new Thread( ) 
			{
				@Override
				public void run( ) 
				{
					Termica.setContext( cordova.getActivity( ) );
					Termica.ImpressaoTexto( text, position, style, size );

					if ( qrcode != null && qrcode.length( ) > 0 ) 
					{
						Termica.setContext( cordova.getActivity( ) );
						Termica.ImpressaoQRCode( qrcode, sizeCode, nivel );
					}		
					
					if ( fecha == 1 )
					{
						if ( corte == 1 ) {
							Termica.Corte( avanco );
						} else if ( avanco > 0 ) 
						{
						   Termica.AvancaPapel( avanco );
						}
								
						Termica.FechaConexaoImpressora( );
					}
				}
			};
			
			try 
			{				
				t.start( );		
					
				callbackContext.success( OK );
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
		
		String text = json.getString( "text" );
		
		int nivel  = Integer.parseInt( json.getString( "nivel"  ) );
		int size   = Integer.parseInt( json.getString( "size"   ) );
		int fecha  = Integer.parseInt( json.getString( "fecha"  ) );
		int corte  = Integer.parseInt( json.getString( "corte"  ) );
		int avanco = Integer.parseInt( json.getString( "avanco" ) );
		
		String OK = "ok";
		
		if ( text != null && text.length( ) > 0 ) 
		{
			Thread t = new Thread( ) 
			{
				@Override
				public void run( ) 
				{
					Termica.setContext( cordova.getActivity( ) );
					Termica.AvancaPapel( 1 );
				    Termica.ImpressaoQRCode( text, size, nivel );

					if ( fecha == 1 )
					{
						if ( corte == 1 ) {
							Termica.Corte( avanco );
						} else if ( avanco > 0 ) 
						{
						   Termica.AvancaPapel( avanco );
						}
								
						Termica.FechaConexaoImpressora( );
					}					
				}
			};
			
			try 
			{
				t.start( );	
				
				callbackContext.success( OK );
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
