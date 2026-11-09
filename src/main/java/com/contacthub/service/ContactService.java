
package com.contacthub.service;

import com.contacthub.model.Contact;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class ContactService {

    private final String fileName = "contacts.dat";

    private ArrayList<Contact> contacts = new ArrayList<>();

    public ContactService() {
        loadContacts();
    }

    public List<Contact> getAllContacts() {
        return sortContacts(contacts);
    }

    // Get contacts by category: Personal or Professional

    public List<Contact> getContactsByCategory(String category) {

        ArrayList<Contact> result = new ArrayList<>();

        for (Contact c : contacts) {

            if (c.getCategory() != null &&
                c.getCategory().equalsIgnoreCase(category)) {

                result.add(c);
            }
        }

        return sortContacts(result);
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
        saveContacts();
    }

    public Contact getContactByPhone(String phone) {

        for (Contact c : contacts) {

            if (c.getPhone() != null &&
                c.getPhone().equals(phone)) {

                return c;
            }
        }

        return null;
    }

    public void updateContact(String oldPhone, Contact updatedContact) {

        Contact existing = getContactByPhone(oldPhone);

        if (existing != null) {

            updatedContact.setFavourite(existing.isFavourite());

            int index = contacts.indexOf(existing);
            contacts.set(index, updatedContact);

            saveContacts();
        }
    }

    public List<Contact> searchContacts(String query) {

        ArrayList<Contact> result = new ArrayList<>();

        if (query == null) {
            query = "";
        }

        String search = query.toLowerCase();

        for (Contact c : contacts) {

            String name = c.getName() == null
                    ? "" : c.getName().toLowerCase();

            String phone = c.getPhone() == null
                    ? "" : c.getPhone();

            if (name.contains(search) || phone.contains(search)) {
                result.add(c);
            }
        }

        return sortContacts(result);
    }

    public List<Contact> getFavourites() {

        ArrayList<Contact> result = new ArrayList<>();

        for (Contact c : contacts) {

            if (c.isFavourite()) {
                result.add(c);
            }
        }

        return sortContacts(result);
    }

    public void toggleFavourite(String phone) {

        Contact contact = getContactByPhone(phone);

        if (contact != null) {

            contact.setFavourite(!contact.isFavourite());

            saveContacts();
        }
    }

    public void deleteContact(String phone) {

        contacts.removeIf(c ->
                c.getPhone() != null &&
                c.getPhone().equals(phone));

        saveContacts();
    }

    public boolean phoneExists(String phone) {

        for (Contact c : contacts) {

            if (c.getPhone() != null &&
                c.getPhone().equals(phone)) {

                return true;
            }
        }

        return false;
    }

    public boolean phoneExistsExcept(String phone, String oldPhone) {

        for (Contact c : contacts) {

            if (c.getPhone() != null &&
                c.getPhone().equals(phone) &&
                !c.getPhone().equals(oldPhone)) {

                return true;
            }
        }

        return false;
    }

    private List<Contact> sortContacts(List<Contact> list) {

        ArrayList<Contact> sorted = new ArrayList<>(list);

        sorted.sort(Comparator.comparing(
            c -> c.getName() == null ? "" : c.getName(),
            String.CASE_INSENSITIVE_ORDER
        ));

        return sorted;
    }

    public Map<String, List<Contact>> groupContacts(List<Contact> list) {

        Map<String, List<Contact>> groups = new TreeMap<>();

        for (Contact contact : sortContacts(list)) {

            String name = contact.getName();

            String letter = "#";

            if (name != null && !name.trim().isEmpty()) {

                String first = name.trim()
                        .substring(0, 1).toUpperCase();

                if (first.matches("[A-Z]")) {
                    letter = first;
                }
            }

            groups.computeIfAbsent(letter, k -> new ArrayList<>())
                    .add(contact);
        }

        return groups;
    }

    private void saveContacts() {

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(fileName))) {

            out.writeObject(contacts);

        } catch (IOException e) {

            throw new RuntimeException("Unable to save contacts", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadContacts() {

        File file = new File(fileName);

        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(file))) {

            contacts = (ArrayList<Contact>) in.readObject();

        } catch (Exception e) {

            throw new RuntimeException("Unable to load saved contacts", e);
        }
    }
}
