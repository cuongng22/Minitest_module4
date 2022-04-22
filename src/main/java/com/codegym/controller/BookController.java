package com.codegym.controller;

import com.codegym.model.Book;
import com.codegym.model.BookForm;
import com.codegym.model.Category;
import com.codegym.service.book.IBookService;
import com.codegym.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    private IBookService bookService;

    @Autowired
    private ICategoryService categoryService;

    @Value("${file-upload}")
    private String fileUpload;

    @ModelAttribute(name = "categories")
    private List<Category> categories() {
        return categoryService.findALl();
    }

    @GetMapping
    public ModelAndView showList(@RequestParam(name = "q")Optional<String > q, @PageableDefault(value = 5)Pageable pageable) {
        Page<Book> books;
        if (!q.isPresent()) {
            books = bookService.findAll(pageable);
        } else {
            books= bookService.findByNameContaining(q.get(), pageable);
        }

        ModelAndView modelAndView  = new ModelAndView("/book/list").addObject("books", books);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showFormCreate() {
        BookForm bookForm = new BookForm();
        ModelAndView modelAndView= new ModelAndView("/book/create").addObject("bookForm", bookForm);
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView create(@ModelAttribute BookForm bookForm) {
        MultipartFile imageFile = bookForm.getAvatar();
        String fileName = imageFile.getOriginalFilename();
        long currentTime = System.currentTimeMillis();
        fileName = currentTime + fileName;
        try {
            FileCopyUtils.copy(imageFile.getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Book book = new Book(bookForm.getName(), bookForm.getPrice(), bookForm.getAuthor(), fileName, bookForm.getCategory());
        bookService.save(book);
        return new ModelAndView("redirect:/book");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/book/edit");
        Optional<Book> book = bookService.findById(id);
        if (!book.isPresent()) {
            return new ModelAndView("error-404");
        }
        modelAndView.addObject("book", book.get());
        modelAndView.addObject("bookForm", new BookForm());
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView edit(@ModelAttribute BookForm bookForm, @PathVariable Long id) {
        Optional<Book> book = this.bookService.findById(id);
        String image;
        MultipartFile imageFile = bookForm.getAvatar();
        if (imageFile.getSize()==0) {
            image = book.get().getAvatar();
        } else {
            String fileName = imageFile.getOriginalFilename();
            long currentTime = System.currentTimeMillis();
            fileName = currentTime + fileName;
            image = fileName;
            try {
                FileCopyUtils.copy(imageFile.getBytes(), new File(fileUpload + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Book newBook = new Book(id,bookForm.getName(), bookForm.getPrice(), bookForm.getAuthor(), image, bookForm.getCategory());
        bookService.save(newBook);
        return new ModelAndView("redirect:/book");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/book/delete");
        Optional<Book> book = bookService.findById(id);
        if (!book.isPresent()) {
            return new ModelAndView("error-404");
        }
        modelAndView.addObject("book",book.get());
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id) {
        bookService.remove(id);
        return new ModelAndView("redirect:/book");
    }

    @GetMapping("/detail/{id}")
    public ModelAndView showDetail(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/book/detail");
        Optional<Book> book = bookService.findById(id);
        if (!book.isPresent()) {
            return new ModelAndView("error-404");
        }
        modelAndView.addObject("book", book.get());
        return modelAndView;
    }

    @GetMapping("/category/{id}")
    public ModelAndView findByCategory(@PathVariable Long id, @PageableDefault(value = 10) Pageable pageable) {
        Page<Book> books = bookService.findByCategory(id, pageable);
        ModelAndView modelAndView = new ModelAndView("/book/list_find");
        return modelAndView.addObject("books", books);
    }

    @GetMapping("/categories")
    public ModelAndView showCategory() {
        ModelAndView modelAndView = new ModelAndView( "/book/category");
        return modelAndView;
    }

}
