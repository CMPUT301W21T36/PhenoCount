// Alexander Filbert, LAB5, 2021-02-25, https://eclass.srv.ualberta.ca/pluginfile.php/6714046/mod_resource/content/0/Lab%205%20Firestore%20Integration%20Instructions.pdf
package com.cmput301w21t36.phenocount;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.* ;

/**
 * Test class for model layer Profile. All related JUnit tests can be found in this class.
 * Note that junit 5 is being used.
 * @author  Caleb Lonson
 */
public class ProfileTest {

    /**
     * This will create a mock profile object for testing
     * @return profile object
     */
    private Profile mockProfile() {
        return new Profile("John","7806766767");
    }

    /**
     * This will create a mock user object for testing
     * @return user object
     */
    private User mockUser() {
        return new User("000000001", mockProfile());
    }

    /**
     * Will test that the username information can be updated with a method
     * and the related user's username will be updated to reflect the change
     */
    @Test
    public void changeUsername() {
        User user = mockUser();
        user.getProfile().setUsername("Alex");
        assertEquals(user.getProfile().getUsername(), "Alex");
    }

    /**
     * Will test that the contact information can be updated with a method
     * and the related user's contact will be updated to reflect the change
     */
    @Test
    public void changeContact() {
        User user = mockUser();
        user.getProfile().setPhone("7801111111");
        assertEquals(user.getProfile().getPhone(), "7801111111");
    }


}

