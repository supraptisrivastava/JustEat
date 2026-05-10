import api from "./axiosConfig";

export const toggleRestaurantFavourite = (publicId) =>
  api.post(`/favourites/restaurants/${publicId}`);

export const toggleMenuItemFavourite = (id) =>
  api.post(`/favourites/menu-items/${id}`);

export const getFavouriteRestaurants = () => api.get("/favourites/restaurants");

export const getFavouriteMenuItems = () => api.get("/favourites/menu-items");

