package meow.dogs.work;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import ice.BaseMessage;
import ice.IceError;
import ice.ping;

import android.os.AsyncTask;

public class MySocket
{
    public WatchData myWd;
    public class WatchData
    {
        public Socket mySocket;
        public ObjectInputStream iS = null;
        public ObjectOutputStream oS = null;
        public BaseMessage BMq;
        public BaseMessage BMa;
        public WatchData(BaseMessage bm) throws IOException
        {
            this.BMq = bm;
            this.BMa = null;
            SocketAddress mySA = new InetSocketAddress(InetAddress.getByName(MainActivity.getIPstr()),Integer.parseInt(MainActivity.Portgen));
            this.mySocket = new Socket();
            this.mySocket.connect(mySA,5000);
            this.oS = new ObjectOutputStream(this.mySocket.getOutputStream());
            this.iS = new ObjectInputStream(this.mySocket.getInputStream());
        }
    }
    class QASocket extends AsyncTask<WatchData , Integer, Integer>
    {
        protected Integer doInBackground(WatchData... param)
        {
            try
            {
                param[0].oS.writeObject(param[0].BMq);
                param[0].BMa = (BaseMessage)param[0].iS.readObject();
            }
            catch (OptionalDataException e)
            {
                param[0].BMa = (BaseMessage) new IceError("OptionalDataException");
            }
            catch (ClassNotFoundException e)
            {
                param[0].BMa = (BaseMessage) new IceError("ClassNotFoundException");
            }
            catch (IOException e)
            {
                param[0].BMa = (BaseMessage) new IceError("IOException");
            }
            return 0;
        }
    }
    class QSocket extends AsyncTask<WatchData , Integer, Integer>
    {
        protected Integer doInBackground(WatchData... param)
        {
            try
            {
                param[0].oS.writeObject(param[0].BMq);
                param[0].mySocket.close();
                param[0].BMa = (BaseMessage) new ping("MySocketClose");
            }
            catch (OptionalDataException e)
            {
                param[0].BMa = (BaseMessage) new IceError("OptionalDataException");
            }
            catch (IOException e)
            {
                param[0].BMa = (BaseMessage) new IceError("IOException");
            }
            return 0;
        }
    }
    //for commit
    static BaseMessage question_answer(BaseMessage obj) throws IOException
    {
        MySocket newSocket=new MySocket();
        newSocket.myWd = newSocket.new WatchData(obj);
        newSocket.new QASocket().execute(newSocket.myWd);
        while(newSocket.myWd.BMa == null)
        {
        }
        return newSocket.myWd.BMa;
    }
    public BaseMessage question_answer()
    {
        this.new QASocket().execute(this.myWd);
        while(this.myWd.BMa == null)
        {
        }
        return this.myWd.BMa;
    }
    public BaseMessage only_question()
    {
        this.new QSocket().execute(this.myWd);
        while(this.myWd.BMa == null)
        {
        }
        return this.myWd.BMa;
    }
    static BaseMessage only_question(BaseMessage obj) throws IOException
    {
        MySocket newSocket=new MySocket();
        newSocket.myWd = newSocket.new WatchData(obj);
        newSocket.new QSocket().execute(newSocket.myWd);
        while(newSocket.myWd.BMa == null)
        {
        }
        return newSocket.myWd.BMa;
    }
}