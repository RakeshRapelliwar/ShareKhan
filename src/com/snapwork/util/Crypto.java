package com.snapwork.util;

public class Crypto
{
	static String encryptString = "";
	static StringBuffer sbr = new StringBuffer();;
	static char mapping[]={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9','-','.'} ;

	public Crypto()
	{	
	}

	private static char getc(int presentCount)
	{	
		char c = 0;
		if(presentCount< encryptString.length())
		{
			c = encryptString.charAt(presentCount);
		}
		return c;
	}

	private static void putc(int i)
	{	
		sbr.append((char)i);
	}

	public static String decrypt(String s1)
	{
		int bitoffset = 0;
		int outchar = 0 ;
		int c=0;
		int mapping1[] = new int[256];
		sbr.delete(0,sbr.length());
		int charCount=0;
		for(int j=0;j<mapping.length; j++)
		{
			mapping1[mapping[j]]=j;
		}
		encryptString = s1;
		c=getc(charCount);
		int t2 =0;
		while(c>0)
		{	
			outchar|=mapping1[c]<<bitoffset;
			bitoffset +=6;
			if(bitoffset >= 8)
			{	
				t2 = outchar&127;
				putc(t2);
				bitoffset-=8;
				outchar >>=8;
			}
			charCount++;
			c=getc(charCount);
		}
		return sbr.toString();
	}
}