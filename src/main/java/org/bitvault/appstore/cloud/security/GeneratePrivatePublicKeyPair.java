package org.bitvault.appstore.cloud.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.HashMap;
import java.util.Map;

import org.bitvault.appstore.cloud.constant.Constants;

/**
 * Class is used to generate a Private Public Key Pair 
 */

public class GeneratePrivatePublicKeyPair {

	public static Map<String, String> generatePrivatePublicKey() throws NoSuchAlgorithmException {
        
		Map<String,String> privatePublicKeyPair = new HashMap<>();
		
		
		try {   	
        	
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDH", "BC");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
            keyGen.initialize(ecSpec, new SecureRandom());
            KeyPair keyPair = keyGen.generateKeyPair();
            ECPublicKey ecPointPublicKey = (ECPublicKey) keyPair.getPublic();
            ECPrivateKey ecPointPrivateKey = (ECPrivateKey) keyPair.getPrivate();


            // Getting Piblic key
            String x = ecPointPublicKey.getW().getAffineX().toString(16);
            String y = ecPointPublicKey.getW().getAffineY().toString(16);

            // check for leading 0's
            String mStringPublicKey;
            if( x.length() < 64 )
                mStringPublicKey = "040" + x;
            else
                mStringPublicKey = "04" + x;

            if( y.length() < 64 )
                mStringPublicKey += "0" + y;
            else
                mStringPublicKey += y;

            privatePublicKeyPair.put(Constants.PUBLIC_KEY, mStringPublicKey) ;

            
            // Getting Public Key
            String x1 = ecPointPrivateKey.getS().toString(16);

            // check for leading 0's
            
            String mStringPrivateKey;
            if( x1.length() < 64 )
                mStringPrivateKey = "1" + x1;
            else
                mStringPrivateKey =  x1;

            privatePublicKeyPair.put(Constants.PRIVATE_KEY, mStringPrivateKey) ;
            

        }catch (Exception e){
            e.printStackTrace();
        }
        return privatePublicKeyPair;
    }
	
}
