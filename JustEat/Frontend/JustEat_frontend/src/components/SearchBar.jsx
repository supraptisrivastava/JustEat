import { useState, useEffect, useCallback } from "react";

const CUISINES = [
  { value: "", label: "All Cuisines" },
  { value: "INDIAN", label: "Indian" },
  { value: "CHINESE", label: "Chinese" },
  { value: "JAPANESE", label: "Japanese" },
  { value: "ITALIAN", label: "Italian" },
  { value: "MEXICAN", label: "Mexican" },
  { value: "CONTINENTAL", label: "Continental" },
  { value: "FRENCH", label: "French" },
  { value: "FAST_FOOD", label: "Fast Food" },
];

const LOCATIONS = [
  { value: "", label: "All Locations" },
  { value: "NOIDA", label: "Noida" },
  { value: "DELHI", label: "Delhi" },
  { value: "GURGAON", label: "Gurgaon" },
];

const SearchBar = ({ onSearch, initialLocation = "" }) => {
  const [keyword, setKeyword] = useState("");
  const [cuisine, setCuisine] = useState("");
  const [location, setLocation] = useState(initialLocation);

  // Debounce function
  const debounce = (func, delay) => {
    let timeoutId;
    return (...args) => {
      clearTimeout(timeoutId);
      timeoutId = setTimeout(() => func(...args), delay);
    };
  };

  // eslint-disable-next-line react-hooks/exhaustive-deps
  const debouncedSearch = useCallback(
    debounce((searchParams) => {
      onSearch(searchParams);
    }, 400),
    [onSearch]
  );

  useEffect(() => {
    const hasSearchCriteria = keyword.trim() || cuisine || location;
    if (hasSearchCriteria) {
      debouncedSearch({
        keyword: keyword.trim() || null,
        cuisine: cuisine || null,
        location: location || null,
      });
    } else {
      onSearch(null);
    }
  }, [keyword, cuisine, location, debouncedSearch, onSearch]);

  const handleClear = () => {
    setKeyword("");
    setCuisine("");
    setLocation("");
    onSearch(null);
  };

  const hasFilters = keyword || cuisine || location;

  return (
    <div className="bg-white dark:bg-gray-800 rounded-xl shadow-md p-4 mb-6">
      <div className="flex flex-col md:flex-row gap-3">
        {/* Keyword Input */}
        <div className="flex-1 relative">
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <svg
              className="h-5 w-5 text-gray-400"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
              />
            </svg>
          </div>
          <input
            type="text"
            placeholder="Search restaurants..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            className="w-full pl-10 pr-4 py-2.5 border border-gray-200 dark:border-gray-600 rounded-lg bg-gray-50 dark:bg-gray-700 text-gray-900 dark:text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all"
          />
        </div>

        {/* Cuisine Dropdown */}
        <select
          value={cuisine}
          onChange={(e) => setCuisine(e.target.value)}
          className="px-4 py-2.5 border border-gray-200 dark:border-gray-600 rounded-lg bg-gray-50 dark:bg-gray-700 text-gray-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent cursor-pointer min-w-[150px]"
        >
          {CUISINES.map((c) => (
            <option key={c.value} value={c.value}>
              {c.label}
            </option>
          ))}
        </select>

        {/* Location Dropdown */}
        <select
          value={location}
          onChange={(e) => setLocation(e.target.value)}
          className="px-4 py-2.5 border border-gray-200 dark:border-gray-600 rounded-lg bg-gray-50 dark:bg-gray-700 text-gray-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent cursor-pointer min-w-[150px]"
        >
          {LOCATIONS.map((l) => (
            <option key={l.value} value={l.value}>
              {l.label}
            </option>
          ))}
        </select>

        {/* Clear Button */}
        {hasFilters && (
          <button
            onClick={handleClear}
            className="px-4 py-2.5 bg-gray-100 dark:bg-gray-600 hover:bg-gray-200 dark:hover:bg-gray-500 text-gray-600 dark:text-gray-200 rounded-lg font-medium transition-colors flex items-center gap-2"
          >
            <svg
              className="h-4 w-4"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
            Clear
          </button>
        )}
      </div>
    </div>
  );
};

export default SearchBar;

