package com.peters.User_Registration_and_Email_Verification.controller;

import com.peters.User_Registration_and_Email_Verification.exception.ApplicationAuthenticationException;
import com.peters.User_Registration_and_Email_Verification.exception.ProductNotFoundException;
import com.peters.User_Registration_and_Email_Verification.product.dto.ProductCategoryRequest;
import com.peters.User_Registration_and_Email_Verification.product.entity.ProductCategory;
import com.peters.User_Registration_and_Email_Verification.product.service.ICategoryService;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.user.dto.UserResponseDto;
import com.peters.User_Registration_and_Email_Verification.user.dto.UserRoleRequestDto;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserRole;
import com.peters.User_Registration_and_Email_Verification.user.service.IRoleService;
import com.peters.User_Registration_and_Email_Verification.user.service.IUserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "admin")
public class AdminController {
    private final ICategoryService categoryService;
    private final IRoleService roleService;
    private final IUserService userService;

    @Operation(
            summary = "fetch all users",
            description = "This endpoint fetches all users from database",
            responses = {
                    @ApiResponse(responseCode = "200",
                    description = "Success",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))
                    ),

                    @ApiResponse(responseCode = "204",
                            description = "NO content",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomResponse.class))
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomResponse.class))
                    )
            }
    )
    @GetMapping("/users")
    public ResponseEntity<CustomResponse> getUser(){
        return userService.getAllUsers();
    }

    @Hidden
    @GetMapping("/all-roles")
    public ResponseEntity<List<UserRole>> getAllRoles(){
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @Operation(
            summary = "Create roles",
            description = "This endpoint allow authenticated admin to crate roles for users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRole.class))
                    ),
                    @ApiResponse(responseCode = "204",
                            description = "NO content",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomResponse.class))
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomResponse.class))
                    )
            }
    )
    @PostMapping("/create-role")
    public ResponseEntity<CustomResponse> createRole(@RequestBody UserRoleRequestDto request){
        return roleService.createRole(request);
    }

    @PostMapping("/remove-all-users-from-role/{id}")
    public ResponseEntity<CustomResponse> removeUserAllUsersFromRole(@PathVariable("id") Long roleId){
        return roleService.removeAllUserFromRole(roleId);
    }

    @PostMapping("/remove-user-from-role")
    public ResponseEntity<CustomResponse> removeSingleUserFromRole(@RequestParam(name = "userId") Long userId, @RequestParam(name = "roleId")Long roleId){
        return roleService.removeUserFromRole(userId, roleId);
    }

    @PostMapping("/assign-user-to-role")
    public ResponseEntity<CustomResponse> assignUserToRole(@RequestParam(name = "userId") Long userId, @RequestParam(name = "roleId")Long roleId){
        return roleService.assignUserToRole(userId, roleId);
    }

    @DeleteMapping("/delete-role/{id}")
    public ResponseEntity<CustomResponse> deleteRole (@PathVariable("id") Long roleId){
        return roleService.deleteRole(roleId);
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
