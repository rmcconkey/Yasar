package com.yasar.yasar;

import java.util.ArrayList;

/**
 * Created by r_mcconkey on 3/24/15.
 */

public interface IdbController {

    public long insertContact(Contact contact);
    public int updateContact(Contact contact);
    public void deleteContact(int id);
    public void deleteAll();
    public ArrayList<Contact> getAllContacts();
    public Contact getContactInfo(int id);

}
