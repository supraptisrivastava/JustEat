import { toast } from "react-toastify";

/**
 * Toast notification utilities for consistent UX feedback
 */

export const showSuccess = (message) => {
  toast.success(message, {
    position: "top-right",
    autoClose: 3000,
  });
};

export const showError = (message) => {
  toast.error(message || "Something went wrong", {
    position: "top-right",
    autoClose: 4000,
  });
};

export const showWarning = (message) => {
  toast.warning(message, {
    position: "top-right",
    autoClose: 3500,
  });
};

export const showInfo = (message) => {
  toast.info(message, {
    position: "top-right",
    autoClose: 3000,
  });
};

// Pre-defined messages for common actions
export const toastMessages = {
  // Auth
  loginSuccess: "Welcome back! You're now logged in.",
  loginError: "Invalid email or password.",
  registerSuccess: "Account created successfully! Please login.",
  logoutSuccess: "You've been logged out.",

  // Cart
  addToCartSuccess: "Item added to cart!",
  removeFromCartSuccess: "Item removed from cart.",
  cartCleared: "Cart has been cleared.",

  // Order
  orderPlaced: "Order placed successfully!",
  orderUpdated: "Order status updated.",

  // Restaurant
  restaurantCreated: "Restaurant created successfully!",
  menuItemAdded: "Menu item added successfully!",
  menuItemUpdated: "Menu item updated successfully!",
  menuItemDeleted: "Menu item deleted.",

  // Errors
  networkError: "Network error. Please check your connection.",
  serverError: "Server error. Please try again later.",
  unauthorized: "Please login to continue.",
  forbidden: "You don't have permission to do this.",
};

