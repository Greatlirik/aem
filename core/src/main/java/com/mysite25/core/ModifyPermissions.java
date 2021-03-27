package com.mysite25.core;

import java.util.NoSuchElementException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;
import org.apache.sling.jcr.api.SlingRepository;
//import org.apache.felix.scr.annotations.Activate;
//import org.apache.felix.scr.annotations.Component;
//import org.apache.felix.scr.annotations.Reference;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlList;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component

public class ModifyPermissions {
    private static final String CONTENT_SITE_FR = "/content/we-retail/fr";
    private static final Logger LOGGER= LoggerFactory.getLogger(ModifyPermissions.class);
    @Reference
    private SlingRepository repo;
    @Activate
    protected void activate(){
        LOGGER.info("ModifyPermissions activated");
        modifyPermissions();
    }
    private void modifyPermissions() {
        Session adminSession = null;
        try{

            adminSession= repo.loginAdministrative(repo.getDefaultWorkspace());
            UserManager userMgr=
                    ((org.apache.jackrabbit.api.JackrabbitSession)adminSession).getUserManager();
            AccessControlPolicyIterator accessControlManager = (AccessControlPolicyIterator) adminSession.getAccessControlManager();
            Authorizable denyAccess = userMgr.getAuthorizable("deny-access");
            AccessControlPolicyIterator policyIterator =
                    ((AccessControlManager) accessControlManager).getApplicablePolicies(CONTENT_SITE_FR);
            AccessControlList acl;
            try{
                acl=(JackrabbitAccessControlList)
                        policyIterator.nextAccessControlPolicy();
            }catch(NoSuchElementException nse){
                acl=(JackrabbitAccessControlList)
                        ((AccessControlManager) accessControlManager).getPolicies(CONTENT_SITE_FR)[0];
            }
            Privilege[] privileges =
                    {((AccessControlManager) accessControlManager).privilegeFromName(Privilege.JCR_READ)};
            acl.addAccessControlEntry(denyAccess.getPrincipal(), privileges);
            ((AccessControlManager) accessControlManager).setPolicy(CONTENT_SITE_FR, acl);
            adminSession.save();
        }catch (RepositoryException e){
            LOGGER.error("**************************Repo Exception", e);
        }finally{
            if (adminSession != null)
                adminSession.logout();
        }
    }
}