import api from "./axiosConfig";

// Order APIs
export const placeOrder = () => api.post("/order/place");

export const getOrderHistory = () => api.get("/order/history");

export const getOrderById = (publicId) => api.get(`/order/${publicId}`);

export const reorder = (publicId) => api.post(`/order/reorder/${publicId}`);
