/** Código fonte do sistema de Controle de Acesso
*   criado para avaliação PROINTER 4 da Faculdade Anhanguera de Campinas Und. 1
*   Data de criação: 05/05/2015 - Sidmar Porfírio
*   Modificação:
*   Equipe: Sidmar Porfírio, Quemuel Aquino, Paulo Victor de Menezes, Leonardo Félix Cuencas
*/

package com.prointer4;

import android.content.Context;
import android.telephony.TelephonyManager;

public class ObterIMEI {
	
	private String IMEI;
	TelephonyManager tManager;
	
	public String getIMEI(Context context) {
		
		tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
		IMEI = tManager.getDeviceId();
		
		return IMEI;
	}
}
