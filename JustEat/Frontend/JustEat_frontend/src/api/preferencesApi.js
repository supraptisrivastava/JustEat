import api from "./axiosConfig";

export const getPreferences = () => api.get("/preferences");

export const savePreferences = (data) => api.put("/preferences", data);

