package com.peters.User_Registration_and_Email_Verification.controller;

import com.peters.User_Registration_and_Email_Verification.product.dto.ProductCategoryRequest;
import com.peters.User_Registration_and_Email_Verification.product.entity.ProductCategory;
import com.peters.User_Registration_and_Email_Verification.product.service.ICategoryService;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.user.dto.UserRoleRequestDto;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserRole;
import com.peters.User_Registration_and_Email_Verification.user.service.IRoleService;
import com.peters.User_Registration_and_Email_Verification.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ICategoryService categoryService;
    private final IRoleService roleService;
    private final IUserService userService;

    @GetMapping("/users")
    public ResponseEntity<CustomResponse> getUser(){
        return userService.getAllUsers();
    }

    @GetMapping("/all-roles")
    public ResponseEntity<List<UserRole>> getAllRoles(){
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping("/create-role")
    public ResponseEntity<UserRole> createRole(@RequestBody UserRoleRequestDto request){
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @PostMapping("/remove-all-users-from-role/{id}")
    public UserRole removeUserAllUsersFromRole(@PathVariable("id") Long roleId){
        return roleService.removeAllUserFromRole(roleId);
    }

    @PostMapping("/remove-user-from-role")
    public UserEntity removeSingleUserFromRole(@RequestParam(name = "userId") Long userId, @RequestParam(name = "roleId")Long roleId){
        return roleService.removeUserFromRole(userId, roleId);
    }

    @PostMapping("/assign-user-to-role")
    public UserEntity assignUserToRole(@RequestParam(name = "userId") Long userId, @RequestParam(name = "roleId")Long roleId){
        return roleService.assignUserToRole(userId, roleId);
    }

    @DeleteMapping("/delete-role/{id}")
    public void deleteRole (@PathVariable("id") Long roleId){
        roleService.deleteRole(roleId);
    }


    @GetMapping("/all-categories")
    public ResponseEntity<List<ProductCategory>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/create-category")
    public ResponseEntity<ProductCategory> createCategory(@RequestBody ProductCategoryRequest request){
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @PostMapping("/remove-all-products-from-category/{id}")
    public ProductCategory removeAllProductFromCategory(@PathVariable("id") Long categoryId){
        return categoryService.removeAllProductFromCategory(categoryId);
    }

    @PostMapping("/remove-product-from-category")
    public ResponseEntity<CustomResponse> removeSingleUserFromCategory(@RequestParam(name = "productId") Long productId, @RequestParam(name = "categoryId")Long categoryId){
        return categoryService.removeProductFromCategory(productId, categoryId);
    }


    @DeleteMapping("/delete-category/{id}")
    public void deleteCategory (@PathVariable("id") Long categoryId){
        categoryService.deleteCategory(categoryId);
    }
}
