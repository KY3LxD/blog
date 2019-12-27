package com.kyel.blog.web.admin;

import com.kyel.blog.NotFoundException;
import com.kyel.blog.po.Blog;
import com.kyel.blog.po.User;
import com.kyel.blog.service.BlogService;
import com.kyel.blog.service.TagService;
import com.kyel.blog.service.TypeService;
import com.kyel.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blogQuery,
                        Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blogQuery,
                         Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        return "admin/blogs :: blogList";
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(Model model, @PathVariable Long id) {
        setTypeAndTag(model);
        Blog b = blogService.getBlog(id);
        b.init();
        model.addAttribute("blog", b);
        return INPUT;
    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));

        Blog b;
        if (blog.getId() == null)
             b = blogService.saveBlog(blog);
        else
            b = blogService.updateBlog(blog.getId(), blog);

        if (b == null) {
            attributes.addFlashAttribute("failure", "操作失败");
        } else {
            attributes.addFlashAttribute("success", "操作成功");
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(RedirectAttributes attributes, @PathVariable Long id) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("deletion", "删除成功");
        return REDIRECT_LIST;

    }

//    @PostMapping("/blogs/{id}")
//    public String editPost(Blog blog, RedirectAttributes attributes, HttpSession session, @PathVariable Long id) {
//        blog.setUser((User) session.getAttribute("user"));
//        blog.setType(typeService.getType(blog.getType().getId()));
//        blog.setTags(tagService.listTag(blog.getTagIds()));
//
//        Blog b = blogService.updateBlog(id, blog);
//        if (b == null) {
//            attributes.addFlashAttribute("failure", "操作失败");
//        } else {
//            attributes.addFlashAttribute("success", "操作成功");
//        }
//        return REDIRECT_LIST;
//    }
}
