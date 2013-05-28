/****************************************************
Statistics Online Computational Resource (SOCR)
http://www.StatisticsResource.org
 
All SOCR programs, materials, tools and resources are developed by and freely disseminated to the entire community.
Users may revise, extend, redistribute, modify under the terms of the Lesser GNU General Public License
as published by the Open Source Initiative http://opensource.org/licenses/. All efforts should be made to develop and distribute
factually correct, useful, portable and extensible resource all available in all digital formats for free over the Internet.
 
SOCR resources are distributed in the hope that they will be useful, but without
any warranty; without any explicit, implicit or implied warranty for merchantability or
fitness for a particular purpose. See the GNU Lesser General Public License for
more details see http://opensource.org/licenses/lgpl-license.php.
 
http://www.SOCR.ucla.edu
http://wiki.stat.ucla.edu/socr
 It s Online, Therefore, It Exists! 
****************************************************/
package edu.ucla.stat.SOCR.JRI;

import java.lang.*;

public class Rengine extends Thread {
    static {
        try {
            System.loadLibrary("jri");
        } catch (UnsatisfiedLinkError e) {
			System.err.println("Cannot find JRI native library!\n");
            e.printStackTrace();
            System.exit(1);
        }
    }

	/**	version of the Rengine itself; see also rniGetVersion() for binary version. It's a good idea for the calling program to check the versions of both and abort if they don't match	*/
    public static long getVersion() {
        return 0x0102;
    }

	public static boolean versionCheck() {
		return (getVersion()==rniGetVersion());
	}
	
    public static int DEBUG=0;
	
    static Rengine mainEngine=null;
    
    public static Rengine getMainEngine() { return mainEngine; }

    boolean died, alive, runLoop, loopRunning;
    String[] args;
    Mutex Rsync;
    RMainLoopCallbacks callback;
    
    public Rengine(String[] args, boolean runMainLoop, RMainLoopCallbacks initialCallbacks) {
        super();
        Rsync=new Mutex();
        died=false;
        alive=false;
        runLoop=runMainLoop;
        loopRunning=false;
        this.args=args;
        callback=initialCallbacks;
        mainEngine=this;
        start();
        while (!alive && !died) yield();
    }

    public native int rniSetupR(String[] args);
    
    synchronized int setupR() {
        return setupR(null);
    }
    
    synchronized int setupR(String[] args) {
        int r=rniSetupR(args);
        if (r==0) {
            alive=true; died=false;
        } else {
            alive=false; died=true;
        }
        return r;
    }
    
    public synchronized native long rniParse(String s, int parts);
    public synchronized native long rniEval(long exp, long rho);
    
    public synchronized native String rniGetString(long exp);
    public synchronized native String[] rniGetStringArray(long exp);
    public synchronized native int[] rniGetIntArray(long exp);
    public synchronized native double[] rniGetDoubleArray(long exp);
    public synchronized native long[] rniGetVector(long exp);

    public synchronized native long rniPutString(String s);
    public synchronized native long rniPutStringArray(String[] a);
    public synchronized native long rniPutIntArray(int [] a);
    public synchronized native long rniPutDoubleArray(double[] a);
    public synchronized native long rniPutVector(long[] exps);
    
    public synchronized native long rniGetAttr(long exp, String name);
    public synchronized native void rniSetAttr(long exp, String name, long attr);

    public synchronized native long rniCons(long head, long tail);
    public synchronized native long rniCAR(long exp);
    public synchronized native long rniCDR(long exp);
    public synchronized native long rniPutList(long[] cont);
    public synchronized native long[] rniGetList(long exp);

    //public static native void rniSetEnv(String key, String val);
    //public static native String rniGetEnv(String key);
	
    public static native long rniGetVersion();
    
    public native int rniStop(int flag);
    
    public synchronized native void rniAssign(String name, long exp, long rho);
    
    public synchronized native int rniExpType(long exp);
    public native void rniRunMainLoop();
    
    public synchronized native void rniIdle();

    public void addMainLoopCallbacks(RMainLoopCallbacks c)
    {
        // we don't really "add", we just replace ... (so far)
        callback = c;
    }

    /** if Rengine was initialized with runMainLoop=false then this method can be used to start the main loop at a later point. It has no effect if the loop is running already. This method returns immediately but the loop will be started once the engine is ready. */
    public void startMainLoop() {
	runLoop=true;
    }
    
    //============ R callback methods =========

    public void jriWriteConsole(String text)
    {
        if (callback!=null) callback.rWriteConsole(this, text);
    }

    public void jriBusy(int which)
    {
        if (callback!=null) callback.rBusy(this, which);
    }

    public String jriReadConsole(String prompt, int addToHistory)
    {
		if (DEBUG>0)
			System.out.println("Rengine.jreReadConsole BEGIN "+Thread.currentThread());
        Rsync.unlock();
        String s=(callback==null)?null:callback.rReadConsole(this, prompt, addToHistory);
        if (!Rsync.safeLock()) {
            String es="\n>>JRI Warning: jriReadConsole detected a possible deadlock ["+Rsync+"]["+Thread.currentThread()+"]. Proceeding without lock, but this is inherently unsafe.\n";
            jriWriteConsole(es);
            System.err.print(es);
        }
		if (DEBUG>0)
			System.out.println("Rengine.jreReadConsole END "+Thread.currentThread());
        return s;
    }

    public void jriShowMessage(String message)
    {
        if (callback!=null) callback.rShowMessage(this, message);
    }
    
    public void jriLoadHistory(String filename)
    {
        if (callback!=null) callback.rLoadHistory(this, filename);
    }

    public void jriSaveHistory(String filename)
    {
        if (callback!=null) callback.rSaveHistory(this, filename);
    }
	
	public String jriChooseFile(int newFile)
    {
        if (callback!=null) return callback.rChooseFile(this, newFile);
		return null;
    }
	
	public void jriFlushConsole()
    {
        if (callback!=null) callback.rFlushConsole(this);
    }
	
    
    //============ "official" API =============

    
    public synchronized REXP eval(String s) {
		if (DEBUG>0)
			System.out.println("Rengine.eval("+s+"): BEGIN "+Thread.currentThread());
        boolean obtainedLock=Rsync.safeLock();
        try {
            /* --- so far, we ignore this, because it can happen when a callback needs an eval which is ok ...
            if (!obtainedLock) {
                String es="\n>>JRI Warning: eval(\""+s+"\") detected a possible deadlock ["+Rsync+"]["+Thread.currentThread()+"]. Proceeding without lock, but this is inherently unsafe.\n";
                jriWriteConsole(es);
                System.err.print(es);
            }
             */
            long pr=rniParse(s, 1);
            if (pr>0) {
                long er=rniEval(pr, 0);
                if (er>0) {
                    REXP x=new REXP(this, er);
                    Rsync.unlock();
                    if (DEBUG>0) System.out.println("Rengine.eval("+s+"): END (OK)"+Thread.currentThread());
                    return x;
                }
            }
        } finally {
            if (obtainedLock) Rsync.unlock();
        }
        if (DEBUG>0) System.out.println("Rengine.eval("+s+"): END (ERR)"+Thread.currentThread());
        return null;
    }
    
    /** This method is very much like {@link eval(String)}, except that it is non-blocking and return <code>null</code> if the engine is busy.
        @param s string to evaluate
        @return result of the evaluation or <code>null</code> if the engine is busy
        */
    public synchronized REXP idleEval(String s) {
        int lockStatus=Rsync.tryLock();
        if (lockStatus==1) return null; // 1=locked by someone else
        boolean obtainedLock=(lockStatus==0);
        try {
            long pr=rniParse(s, 1);
            if (pr>0) {
                long er=rniEval(pr, 0);
                if (er>0) {
                    REXP x=new REXP(this, er);
                    Rsync.unlock();
                    return x;
                }
            }
        } finally {
            if (obtainedLock) Rsync.unlock();
        }
        return null;
    }
    
    public synchronized boolean waitForR() {
        return alive;
    }

    public void end() {
        alive=false;
        interrupt();
    }
    
    public void run() {
		if (DEBUG>0)
			System.out.println("Starting R...");
        if (setupR(args)==0) {
            while (alive) {
                try {
                    if (runLoop) {                        
						if (DEBUG>0)
							System.out.println("***> launching main loop:");
                        loopRunning=true;
                        rniRunMainLoop();
                        loopRunning=false;
						if (DEBUG>0)
							System.out.println("***> main loop finished:");
                        System.exit(0);
                    }
                    sleep(100);
                    rniIdle();
                } catch (InterruptedException ie) {
                    interrupted();
                }
            }
            died=true;
			if (DEBUG>0)
				System.out.println("Terminating R thread.");
        } else {
			System.err.println("Unable to start R");
        }
    }
}
