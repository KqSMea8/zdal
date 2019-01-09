/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.connectionmanager;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;

import com.alipay.zdal.datasource.Service;
import com.alipay.zdal.datasource.resource.JBossResourceException;
import com.alipay.zdal.datasource.resource.ResourceException;
import com.alipay.zdal.datasource.resource.spi.ConnectionEvent;
import com.alipay.zdal.datasource.resource.spi.ConnectionRequestInfo;
import com.alipay.zdal.datasource.resource.spi.ManagedConnection;
import com.alipay.zdal.datasource.resource.spi.ManagedConnectionFactory;
import com.alipay.zdal.datasource.resource.util.NotImplementedException;
import com.alipay.zdal.datasource.tm.TransactionTimeoutConfiguration;
import com.alipay.zdal.datasource.transaction.RollbackException;
import com.alipay.zdal.datasource.transaction.SystemException;
import com.alipay.zdal.datasource.transaction.Transaction;
import com.alipay.zdal.datasource.transaction.TransactionManager;

/**
 * The BaseConnectionManager2 is an abstract base class for JBoss
 * ConnectionManager implementations. It includes functionality to obtain
 * managed connections from a ManagedConnectionPool mbean, find the Subject from
 * a SubjectSecurityDomain, and interact with the CachedConnectionManager for
 * connections held over transaction and method boundaries. Important mbean
 * references are to a ManagedConnectionPool supplier (typically a
 * JBossManagedConnectionPool), and a RARDeployment representing the
 * ManagedConnectionFactory.
 *
 *
 * 
 * @author 伯牙
 * @version $Id: BaseConnectionManager2.java, v 0.1 2014-1-6 下午05:32:58 Exp $
 */
public abstract class BaseConnectionManager2 implements ConnectionCacheListener,
                                            ConnectionListenerFactory,
                                            TransactionTimeoutConfiguration, Service {
    private static final Logger       log = Logger.getLogger(BaseConnectionManager2.class);

    protected ManagedConnectionPool   poolingStrategy;

    protected String                  name;

    protected CachedConnectionManager ccm;

    protected boolean                 trace;

    /**
     * Rethrow a throwable as resource exception
     *
     * @deprecated use JBossResourceException.rethrowAsResourceException
     */
    @Deprecated
    protected static void rethrowAsResourceException(String message, Throwable t)
                                                                                 throws ResourceException {
        JBossResourceException.rethrowAsResourceException(message, t);
    }

    /**
     * Default BaseConnectionManager2 managed constructor for use by subclass
     * mbeans.
     */
    public BaseConnectionManager2() {
        super();
        trace = log.isDebugEnabled();
    }

    /**
     * Creates a new <code>BaseConnectionManager2</code> instance. for TESTING
     * ONLY! not a managed operation.
     *
     * @param ccm a <code>CachedConnectionManager</code> value
     * @param poolingStrategy a <code>ManagedConnectionPool</code> value
     */
    public BaseConnectionManager2(CachedConnectionManager ccm, ManagedConnectionPool poolingStrategy) {
        super();
        this.ccm = ccm;
        this.poolingStrategy = poolingStrategy;
        trace = log.isDebugEnabled();
    }

    public void setCachedConnectionManager(CachedConnectionManager cachedConnectionManager) {
        this.ccm = cachedConnectionManager;
    }

    /**
     * For testing
     */
    public ManagedConnectionPool getPoolingStrategy() {
        return poolingStrategy;
    }

    public void setPoolingStrategy(ManagedConnectionPool poolingStrategy) {
        this.poolingStrategy = poolingStrategy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ManagedConnectionFactory getManagedConnectionFactory() {
        return poolingStrategy.getManagedConnectionFactory();
    }

    public BaseConnectionManager2 getInstance() {
        return this;
    }

    public long getTimeLeftBeforeTransactionTimeout(boolean errorRollback) throws RollbackException {
        return -1;
    }

    public int getTransactionTimeout() throws SystemException {
        throw new NotImplementedException("NYI: getTransactionTimeout()");
    }

    /** 
     * @see com.alipay.zdal.datasource.Service#startService()
     */
    public void startService() {
        poolingStrategy.setConnectionListenerFactory(this);

        // Give it somewhere to tell people things
        //        String categoryName = poolingStrategy.getManagedConnectionFactory().getClass().getName()
        //                              + "." + name;

        //        LogWriter没用
        //        PrintWriter logWriter = new LoggerPluginWriter(log.getLoggerPlugin());
        //        try {
        //            poolingStrategy.getManagedConnectionFactory().setLogWriter(logWriter);
        //        } catch (ResourceException re) {
        //            log.warn("Unable to set log writer '" + logWriter + "' on "
        //                     + "managed connection factory", re);
        //            log.warn("Linked exception:", re.getLinkedException());
        //        }

        if (poolingStrategy instanceof PreFillPoolSupport) {
            PreFillPoolSupport prefill = (PreFillPoolSupport) poolingStrategy;
            if (prefill.shouldPreFill()) {
                prefill.prefill();
            }
        }
    }

    /** 
     * @see com.alipay.zdal.datasource.Service#stopService()
     */
    public void stopService() throws Exception {
        poolingStrategy.setConnectionListenerFactory(null);
        poolingStrategy = null;
    }

    /**
     * Public for use in testing pooling functionality by itself. called by both
     * allocateConnection and reconnect.
     *
     * @param subject a <code>Subject</code> value
     * @param cri a <code>ConnectionRequestInfo</code> value
     * @return a <code>ManagedConnection</code> value
     * @exception ResourceException if an error occurs
     */
    public ConnectionListener getManagedConnection(Subject subject, ConnectionRequestInfo cri)
                                                                                              throws ResourceException {
        return getManagedConnection(null, subject, cri);
    }

    /**
     * Get the managed connection from the pool
     *
     * @param transaction the transaction for track by transaction
     * @param subject the subject
     * @param cri the ConnectionRequestInfo
     * @return a managed connection
     * @exception ResourceException if an error occurs
     */
    protected ConnectionListener getManagedConnection(Transaction transaction, Subject subject,
                                                      ConnectionRequestInfo cri)
                                                                                throws ResourceException {
        return poolingStrategy.getConnection(transaction, subject, cri);
    }

    /**
     * 
     * @param cl
     * @param kill
     */
    public void returnManagedConnection(ConnectionListener cl, boolean kill) {
        ManagedConnectionPool localStrategy = cl.getManagedConnectionPool();
        if (localStrategy != poolingStrategy) {
            kill = true;
        }

        try {
            if (kill == false && cl.getState() == ConnectionListener.NORMAL)
                cl.tidyup();
        } catch (Throwable t) {
            log.warn("Error during tidyup " + cl, t);
            kill = true;
        }

        try {
            localStrategy.returnConnection(cl, kill);
        } catch (ResourceException re) {
            // We can receive notification of an error on the connection
            // before it has been assigned to the pool. Reduce the noise for
            // these errors
            if (log.isDebugEnabled() && kill) {
                log.debug("resourceException killing connection (error retrieving from pool?)", re);
            } else if (log.isDebugEnabled() && !kill) {
                log
                    .warn("resourceException returning connection: " + cl.getManagedConnection(),
                        re);
            }

        }
    }

    public int getConnectionCount() {
        return poolingStrategy.getConnectionCount();
    }

    // implementation of javax.resource.spi.ConnectionManager interface

    public Object allocateConnection(ManagedConnectionFactory mcf, ConnectionRequestInfo cri)
                                                                                             throws ResourceException {
        if (poolingStrategy == null) {
            throw new ResourceException(
                "You are trying to use a connection factory that has been shut down: ManagedConnectionFactory is null.");

        }

        // it is an explicit spec requirement that equals be used for matching
        // rather than ==.
        if (!poolingStrategy.getManagedConnectionFactory().equals(mcf)) {
            throw new ResourceException(
                "Wrong ManagedConnectionFactory sent to allocateConnection!");
        }

        // Pick a managed connection from the pool
        Subject subject = getSubject();
        ConnectionListener cl = getManagedConnection(subject, cri);

        // Tell each connection manager the managed connection is active
        reconnectManagedConnection(cl);

        // Ask the managed connection for a connection
        Object connection = null;
        try {
            connection = cl.getManagedConnection().getConnection(subject, cri);
        } catch (Throwable t) {
            managedConnectionDisconnected(cl);
            JBossResourceException.rethrowAsResourceException(
                "Unchecked throwable in ManagedConnection.getConnection() cl=" + cl, t);
        }

        // Associate managed connection with the connection
        registerAssociation(cl, connection);
        if (ccm != null) {
            ccm.registerConnection(this, cl, connection, cri);
        }

        return connection;
    }

    // ConnectionCacheListener implementation

    /** 
     * @see com.alipay.zdal.datasource.resource.connectionmanager.ConnectionCacheListener#transactionStarted(java.util.Collection)
     */
    public void transactionStarted(Collection conns) throws SystemException {
        // reimplement in subclasses
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.connectionmanager.ConnectionCacheListener#reconnect(java.util.Collection, java.util.Set)
     */
    public void reconnect(Collection conns, Set unsharableResources) throws ResourceException {
        // if we have an unshareable connection the association was not removed
        // nothing to do
        if (unsharableResources.contains(name)) {
            if (log.isDebugEnabled()) {
                log.debug("reconnect for unshareable connection: nothing to do");
            }

            return;
        }

        Map criToCLMap = new HashMap();
        for (Iterator i = conns.iterator(); i.hasNext();) {
            ConnectionRecord cr = (ConnectionRecord) i.next();
            if (cr.cl != null) {
                // This might well be an error.
                log.warn("reconnecting a connection handle that still has a managedConnection! "
                         + cr.cl.getManagedConnection() + " " + cr.connection);
            }
            ConnectionListener cl = (ConnectionListener) criToCLMap.get(cr.cri);
            if (cl == null) {
                cl = getManagedConnection(getSubject(), cr.cri);
                criToCLMap.put(cr.cri, cl);
                // only call once per managed connection, when we get it.
                reconnectManagedConnection(cl);
            }

            cl.getManagedConnection().associateConnection(cr.connection);
            registerAssociation(cl, cr.connection);
            cr.setConnectionListener(cl);
        }
        criToCLMap.clear();// not needed logically, might help the gc.
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.connectionmanager.ConnectionCacheListener#disconnect(java.util.Collection, java.util.Set)
     */
    public void disconnect(Collection crs, Set unsharableResources) throws ResourceException {
        // if we have an unshareable connection do not remove the association
        // nothing to do
        if (unsharableResources.contains(name)) {

            if (log.isDebugEnabled()) {
                log.debug("disconnect for unshareable connection: nothing to do");
            }

            return;
        }

        Set cls = new HashSet();
        for (Iterator i = crs.iterator(); i.hasNext();) {
            ConnectionRecord cr = (ConnectionRecord) i.next();
            ConnectionListener cl = cr.cl;
            cr.setConnectionListener(null);
            unregisterAssociation(cl, cr.connection);
            if (!cls.contains(cl)) {
                cls.add(cl);
            }
        }
        for (Iterator i = cls.iterator(); i.hasNext();)
            disconnectManagedConnection((ConnectionListener) i.next());
    }

    // protected methods

    // does NOT put the mc back in the pool if no more handles. Doing so would
    // introduce a race condition
    // whereby the mc got back in the pool while still enlisted in the tx.
    // The mc could be checked out again and used before the delist occured.
    protected void unregisterAssociation(ConnectionListener cl, Object c) throws ResourceException {
        cl.unregisterConnection(c);
    }

    /**
     * Invoked to reassociate a managed connection
     *
     * @param cl the managed connection
     */
    protected void reconnectManagedConnection(ConnectionListener cl) throws ResourceException {
        try {
            // WRONG METHOD NAME!!
            managedConnectionReconnected(cl);
        } catch (Throwable t) {
            disconnectManagedConnection(cl);
            JBossResourceException.rethrowAsResourceException(
                "Unchecked throwable in managedConnectionReconnected() cl=" + cl, t);
        }
    }

    /**
     * Invoked when a managed connection is no longer associated
     *
     * @param cl the managed connection
     */
    protected void disconnectManagedConnection(ConnectionListener cl) {
        try {
            managedConnectionDisconnected(cl);
        } catch (Throwable t) {
            log.warn("Unchecked throwable in managedConnectionDisconnected() cl=" + cl, t);
        }
    }

    protected final CachedConnectionManager getCcm() {
        return ccm;
    }

    /**
     * For polymorphism.
     * <p>
     *
     * Do not invoke directly use reconnectManagedConnection which does the
     * relevent exception handling
     */
    protected void managedConnectionReconnected(ConnectionListener cl) throws ResourceException {
    }

    /**
     * For polymorphism.
     * <p>
     *
     * Do not invoke directly use disconnectManagedConnection which does the
     * relevent exception handling
     */
    protected void managedConnectionDisconnected(ConnectionListener cl) throws ResourceException {
    }

    /**
     * 
     * @param cl
     * @param c
     * @throws ResourceException
     */
    private void registerAssociation(ConnectionListener cl, Object c) throws ResourceException {
        cl.registerConnection(c);
    }

    private Subject getSubject() {
        // TODO: 当前不支持Subject
        Subject subject = null;
        // if (securityDomain != null)
        // {
        // /* Authenticate using the caller info and obtain a copy of the
        // Subject
        // state for use in establishing a secure connection. A copy must be
        // obtained to avoid problems with multiple threads associated with
        // the same principal changing the state of the resulting Subject.
        // */
        // Principal principal = GetPrincipalAction.getPrincipal();
        // Object credential = GetCredentialAction.getCredential();
        // subject = new Subject();
        // if (securityDomain.isValid(principal, credential, subject) == false)
        // throw new
        // SecurityException("Invalid authentication attempt, principal=" +
        // principal);
        // }
        return subject;
    }

    // ConnectionListenerFactory

    public boolean isTransactional() {
        return false;
    }

    public TransactionManager getTransactionManagerInstance() {
        return null;
    }

    // ConnectionListener

    protected abstract class BaseConnectionEventListener implements ConnectionListener {
        private final ManagedConnection     mc;

        private final ManagedConnectionPool mcp;

        private final Object                context;

        private int                         state     = NORMAL;

        private final List                  handles   = new LinkedList();

        private long                        lastUse;

        private boolean                     trackByTx = false;

        private boolean                     permit    = false;

        protected Logger                    log;

        protected boolean                   trace;

        protected long                      lastValidated;

        protected BaseConnectionEventListener(ManagedConnection mc, ManagedConnectionPool mcp,
                                              Object context, Logger log) {
            this.mc = mc;
            this.mcp = mcp;
            this.context = context;
            this.log = log;
            trace = log.isDebugEnabled();
            lastUse = System.currentTimeMillis();
        }

        public ManagedConnection getManagedConnection() {
            return mc;
        }

        public ManagedConnectionPool getManagedConnectionPool() {
            return mcp;
        }

        public Object getContext() {
            return context;
        }

        public int getState() {
            return state;
        }

        public void setState(int newState) {
            this.state = newState;
        }

        public boolean isTimedOut(long timeout) {
            return lastUse < timeout;
        }

        public void used() {
            lastUse = System.currentTimeMillis();
        }

        public boolean isTrackByTx() {
            return trackByTx;
        }

        public void setTrackByTx(boolean trackByTx) {
            this.trackByTx = trackByTx;
        }

        public void tidyup() throws ResourceException {
        }

        public synchronized void registerConnection(Object handle) {
            handles.add(handle);
        }

        /** 
         * @see com.alipay.zdal.datasource.resource.connectionmanager.ConnectionListener#unregisterConnection(java.lang.Object)
         */
        public synchronized void unregisterConnection(Object handle) {
            if (!handles.remove(handle)) {
                if (log.isDebugEnabled()) {
                    log.debug("Unregistered handle that was not registered! " + handle
                              + " for managedConnection: " + mc);
                }
            }
            if (trace) {
                if (log.isDebugEnabled()) {
                    log.debug("unregisterConnection: " + handles.size() + " handles left");
                }
            }
        }

        public synchronized boolean isManagedConnectionFree() {
            return handles.isEmpty();
        }

        protected synchronized void unregisterConnections() {
            try {
                for (Iterator i = handles.iterator(); i.hasNext();) {
                    getCcm().unregisterConnection(BaseConnectionManager2.this, i.next());
                }
            } finally {
                handles.clear();
            }
        }

        /** 
         * @see com.alipay.zdal.datasource.resource.spi.ConnectionEventListener#connectionErrorOccurred(com.alipay.zdal.datasource.resource.spi.ConnectionEvent)
         */
        public void connectionErrorOccurred(ConnectionEvent ce) {
            if (state == NORMAL) {
                if (ce != null) {
                    Throwable t = ce.getException();
                    if (t == null)
                        t = new Exception("No exception was reported");
                    log.warn("Connection error occured: " + this, t);
                } else {
                    Throwable t = new Exception("No exception was reported");
                    log.warn("Unknown Connection error occured: " + this, t);
                }
            }
            try {
                unregisterConnections();
            } catch (Throwable t) {
                // ignore, it wasn't checked out.
            }
            if (ce != null && ce.getSource() != getManagedConnection())
                log.warn("Notified of error on a different managed connection?");
            returnManagedConnection(this, true);
        }

        public void enlist() throws SystemException {
        }

        public void delist() throws ResourceException {
        }

        public boolean hasPermit() {
            return permit;
        }

        public void grantPermit(boolean value) {
            this.permit = value;
        }

        public long getLastValidatedTime() {
            return this.lastValidated;
        }

        public void setLastValidatedTime(long lastValidated) {
            this.lastValidated = lastValidated;
        }

        // For debugging
        /** 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuffer buffer = new StringBuffer(100);
            buffer.append(getClass().getName()).append('@').append(
                Integer.toHexString(System.identityHashCode(this)));
            buffer.append("[state=");
            if (state == ConnectionListener.NORMAL)
                buffer.append("NORMAL");
            else if (state == ConnectionListener.DESTROY)
                buffer.append("DESTROY");
            else if (state == ConnectionListener.DESTROYED)
                buffer.append("DESTROYED");
            else
                buffer.append("UNKNOWN?");
            buffer.append(" mc=").append(mc);
            buffer.append(" handles=").append(handles.size());
            buffer.append(" lastUse=").append(lastUse);
            buffer.append(" permit=").append(permit);
            buffer.append(" trackByTx=").append(trackByTx);
            buffer.append(" mcp=").append(mcp);
            buffer.append(" context=").append(context);
            toString(buffer);
            buffer.append(']');
            return buffer.toString();
        }

        // For debugging
        protected void toString(StringBuffer buffer) {
        }
    }

    // private static class GetPrincipalAction implements PrivilegedAction
    // {
    // static PrivilegedAction ACTION = new GetPrincipalAction();
    //
    // public Object run()
    // {
    // Principal principal = SecurityAssociation.getPrincipal();
    // return principal;
    // }
    //
    // static Principal getPrincipal()
    // {
    // Principal principal = (Principal) AccessController.doPrivileged(ACTION);
    // return principal;
    // }
    // }

    // private static class GetCredentialAction implements PrivilegedAction
    // {
    // static PrivilegedAction ACTION = new GetCredentialAction();
    //
    // public Object run()
    // {
    // Object credential = SecurityAssociation.getCredential();
    // return credential;
    // }
    //
    // static Object getCredential()
    // {
    // Object credential = AccessController.doPrivileged(ACTION);
    // return credential;
    // }
    // }
}
