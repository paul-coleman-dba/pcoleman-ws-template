/*
    A simple no-package class to facilitate encrypt/decrypt of string command-line vars
 */
public class CipherTool
{
    public static void main(String[] args)
    {
        if (0 != args.length)
        {
            if ("encrypt".equalsIgnoreCase(args[0]))
            {
                System.out.println( com.template.framework.utilities.DefaultCipher.encrypt(args[1]));
            }
            else
            {
                System.out.println( com.template.framework.utilities.DefaultCipher.decrypt(args[1]));
            }
        }
        else
        {
            System.out.println( "Usage: \n\tset environment variables, e.g., export AES_KEY_16=[16-byte string]\n\tmvn clean install\n\tjava  -cp target/pcoleman-ws-template.jar  DefaultCipher [encrypt | decrypt] [text]");
        }
    }
}