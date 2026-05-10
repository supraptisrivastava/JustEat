import { useState } from "react";
import { Link } from "react-router-dom";
import { toggleMenuItemFavourite } from "../api/favouritesApi";
import { useAuth } from "../context/AuthContext";

const dietaryColor = {
  VEG: "text-green-600 bg-green-50 border-green-200",
  VEGAN: "text-emerald-600 bg-emerald-50 border-emerald-200",
  NON_VEG: "text-red-600 bg-red-50 border-red-200",
  EGG: "text-yellow-600 bg-yellow-50 border-yellow-200",
  JAIN: "text-purple-600 bg-purple-50 border-purple-200",
  GLUTEN_FREE: "text-blue-600 bg-blue-50 border-blue-200",
};

const MenuItemCard = ({ item, initialFavourited = false, onFavouriteToggle }) => {
  const { role } = useAuth();
  const [favoured, setFavoured] = useState(initialFavourited);
  const [toggling, setToggling] = useState(false);

  const handleFavourite = async (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (toggling) return;
    setToggling(true);
    try {
      await toggleMenuItemFavourite(item.id);
      const next = !favoured;
      setFavoured(next);
      if (onFavouriteToggle) onFavouriteToggle(item.id, next);
    } catch {
      // silent
    } finally {
      setToggling(false);
    }
  };

  const dietCls = `text-xs font-semibold px-2 py-0.5 rounded-full border capitalize ${dietaryColor[item.dietaryRestriction] || "text-gray-600 bg-gray-50 border-gray-200"}`;

  const cardContent = (
    <div className="bg-white dark:bg-gray-800 rounded-xl overflow-hidden shadow-md hover:-translate-y-1 hover:shadow-xl transition-all flex-shrink-0 w-52">
      <div className="relative">
        {item.imageUrl ? (
          <img src={item.imageUrl} alt={item.name} className="w-full h-36 object-cover"
            onError={(e) => { e.target.style.display = "none"; }} />
        ) : (
          <div className="w-full h-36 bg-gradient-to-br from-amber-100 to-orange-200 dark:from-amber-900/30 dark:to-orange-800/30 flex items-center justify-center text-4xl">
            🍽️
          </div>
        )}
        {item.isMostlyOrdered && (
          <div className="absolute top-2 left-2 bg-orange-500 text-white text-xs font-bold px-2 py-0.5 rounded-full">
            🔥 Popular
          </div>
        )}
        {!item.isMostlyOrdered && item.isSpecial && (
          <div className="absolute top-2 left-2 bg-purple-500 text-white text-xs font-bold px-2 py-0.5 rounded-full">
            ⭐ Special
          </div>
        )}
        {role === "CUSTOMER" && (
          <button
            onClick={handleFavourite}
            className={`absolute top-2 right-2 w-7 h-7 rounded-full flex items-center justify-center text-sm shadow transition-all ${
              favoured ? "bg-red-500 text-white" : "bg-white/80 text-gray-400 hover:text-red-500"
            }`}
          >
            {favoured ? "❤️" : "🤍"}
          </button>
        )}
      </div>
      <div className="p-3">
        <div className="font-bold text-sm mb-1 truncate">{item.name}</div>
        <div className="text-gray-500 dark:text-gray-400 text-xs mb-2 line-clamp-2">{item.description}</div>
        <div className="flex items-center justify-between">
          <span className="font-bold text-orange-500 text-sm">₹{item.price}</span>
          {item.dietaryRestriction && (
            <span className={dietCls}>{item.dietaryRestriction.replace("_", " ")}</span>
          )}
        </div>
      </div>
    </div>
  );

  if (item.restaurantPublicId) {
    return (
      <Link to={`/restaurant/${item.restaurantPublicId}`} className="no-underline text-inherit block">
        {cardContent}
      </Link>
    );
  }

  return cardContent;
};

export default MenuItemCard;

