
package com.contacthub.controller;

import com.contacthub.model.Contact;
import com.contacthub.service.ContactService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ContactController {

    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    // Add navigation counts to every page

    @ModelAttribute
    public void addNavigationCounts(Model model) {

        model.addAttribute("allContactsCount",
                service.getAllContacts().size());

        model.addAttribute("personalCount",
                service.getContactsByCategory("Personal").size());

        model.addAttribute("professionalCount",
                service.getContactsByCategory("Professional").size());

        model.addAttribute("favouritesCount",
                service.getFavourites().size());
    }

    // Display contacts and alphabetical groups

    private void showContacts(List<Contact> contacts, Model model) {

        model.addAttribute("contacts", contacts);

        model.addAttribute("contactGroups",
                service.groupContacts(contacts));
    }

    // All Contacts

    @GetMapping("/")
    public String home(Model model) {

        showContacts(service.getAllContacts(), model);

        model.addAttribute("query", "");

        return "index";
    }

    // Personal Contacts

    @GetMapping("/personal")
    public String personal(Model model) {

        showContacts(
                service.getContactsByCategory("Personal"), model);

        model.addAttribute("query", "");

        return "index";
    }

    // Professional Contacts

    @GetMapping("/professional")
    public String professional(Model model) {

        showContacts(
                service.getContactsByCategory("Professional"), model);

        model.addAttribute("query", "");

        return "index";
    }

    // Add Contact

    @PostMapping("/add")
    public String addContact(@ModelAttribute Contact contact) {

        if (contact.getName() == null ||
            contact.getName().trim().isEmpty() ||
            contact.getPhone() == null ||
            !contact.getPhone().matches("[0-9]{10}") ||
            service.phoneExists(contact.getPhone())) {

            return "redirect:/";
        }

        if (contact.getAlternatePhone() != null &&
            !contact.getAlternatePhone().isBlank() &&
            !contact.getAlternatePhone().matches("[0-9]{10}")) {

            return "redirect:/";
        }

        // Accept only Personal or Professional categories

        if (!"Personal".equals(contact.getCategory()) &&
            !"Professional".equals(contact.getCategory())) {

            contact.setCategory("Personal");
        }

        service.addContact(contact);

        return "redirect:/";
    }

    // Search Contacts

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "") String q,
                         Model model) {

        showContacts(service.searchContacts(q), model);

        model.addAttribute("query", q);

        return "index";
    }

    // Favourite Contacts

    @GetMapping("/favourites")
    public String favourites(Model model) {

        showContacts(service.getFavourites(), model);

        model.addAttribute("query", "");

        return "index";
    }

    // Toggle Favourite

    @PostMapping("/favourite/{phone}")
    public String toggleFavourite(@PathVariable String phone) {

        service.toggleFavourite(phone);

        return "redirect:/";
    }

    // Delete Contact

    @PostMapping("/delete/{phone}")
    public String deleteContact(@PathVariable String phone) {

        service.deleteContact(phone);

        return "redirect:/";
    }

    // View Contact

    @GetMapping("/view/{phone}")
    public String viewContact(@PathVariable String phone, Model model) {

        Contact contact = service.getContactByPhone(phone);

        if (contact == null) {
            return "redirect:/";
        }

        model.addAttribute("contact", contact);

        return "view";
    }

    // Edit Contact

    @GetMapping("/edit/{phone}")
    public String editContact(@PathVariable String phone, Model model) {

        Contact contact = service.getContactByPhone(phone);

        if (contact == null) {
            return "redirect:/";
        }

        model.addAttribute("contact", contact);

        return "edit";
    }

    // Update Contact

    @PostMapping("/update/{oldPhone}")
    public String updateContact(
            @PathVariable String oldPhone,
            @ModelAttribute Contact contact) {

        if (contact.getName() == null ||
            contact.getName().trim().isEmpty() ||
            contact.getPhone() == null ||
            !contact.getPhone().matches("[0-9]{10}") ||
            service.phoneExistsExcept(contact.getPhone(), oldPhone)) {

            return "redirect:/edit/" + oldPhone;
        }

        if (contact.getAlternatePhone() != null &&
            !contact.getAlternatePhone().isBlank() &&
            !contact.getAlternatePhone().matches("[0-9]{10}")) {

            return "redirect:/edit/" + oldPhone;
        }

        // Accept only Personal or Professional categories

        if (!"Personal".equals(contact.getCategory()) &&
            !"Professional".equals(contact.getCategory())) {

            contact.setCategory("Personal");
        }

        service.updateContact(oldPhone, contact);

        return "redirect:/view/" + contact.getPhone();
    }
}
