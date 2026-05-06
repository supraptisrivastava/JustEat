import { useState, useEffect } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { resetPassword } from "../auth/authService";

const inputCls =
  "w-full px-3 py-2.5 border-2 border-gray-200 dark:border-gray-600 rounded-lg text-sm bg-white dark:bg-gray-700 text-gray-900 dark:text-white focus:outline-none focus:border-orange-500 transition-colors";
const labelCls =
  "text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider";

const ResetPassword = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");

  const [form, setForm] = useState({ newPassword: "", confirmPassword: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    if (!token) {
      setError("Invalid or missing reset token. Please request a new password reset link.");
    }
  }, [token]);

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    // Validate passwords match
    if (form.newPassword !== form.confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    // Validate password length
    if (form.newPassword.length < 6) {
      setError("Password must be at least 6 characters");
      return;
    }

    setLoading(true);
    try {
      await resetPassword(token, form.newPassword);
      setSuccess(true);
      // Redirect to login after 3 seconds
      setTimeout(() => navigate("/login"), 3000);
    } catch (err) {
      setError(err.response?.data?.message || "Failed to reset password. The link may have expired.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-orange-50 to-white dark:from-gray-900 dark:to-gray-800 p-4">
      <div className="bg-white dark:bg-gray-800 rounded-xl shadow-2xl p-10 w-full max-w-md">
        <div className="text-3xl font-extrabold text-orange-500 mb-1">
          Just<span className="text-gray-900 dark:text-white">Eat</span>
        </div>
        <p className="text-gray-500 dark:text-gray-400 text-sm mb-8">
          Create a new password for your account
        </p>

        {error && (
          <div className="bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-400 border border-red-200 dark:border-red-800 rounded-lg px-4 py-3 text-sm mb-4">
            {error}
          </div>
        )}

        {success ? (
          <div className="text-center">
            <div className="bg-green-50 dark:bg-green-900/20 text-green-700 dark:text-green-400 border border-green-200 dark:border-green-800 rounded-lg px-4 py-4 text-sm mb-6">
              <div className="text-2xl mb-2">✅</div>
              <p className="font-semibold">Password Reset Successful!</p>
              <p className="mt-1 text-xs">
                Redirecting to login page...
              </p>
            </div>
            <Link
              to="/login"
              className="text-orange-500 hover:underline text-sm font-semibold"
            >
              Go to Login Now →
            </Link>
          </div>
        ) : (
          <form onSubmit={handleSubmit}>
            <div className="grid gap-4 mb-6">
              <div className="flex flex-col gap-1.5">
                <label className={labelCls}>New Password</label>
                <input
                  type="password"
                  name="newPassword"
                  placeholder="Min. 6 characters"
                  value={form.newPassword}
                  onChange={handleChange}
                  required
                  minLength={6}
                  disabled={!token}
                  className={inputCls}
                />
              </div>
              <div className="flex flex-col gap-1.5">
                <label className={labelCls}>Confirm Password</label>
                <input
                  type="password"
                  name="confirmPassword"
                  placeholder="Repeat your password"
                  value={form.confirmPassword}
                  onChange={handleChange}
                  required
                  minLength={6}
                  disabled={!token}
                  className={inputCls}
                />
              </div>
            </div>

            <button
              type="submit"
              disabled={loading || !token}
              className="w-full bg-orange-500 hover:bg-orange-600 disabled:opacity-50 text-white font-semibold text-sm px-4 py-2.5 rounded-lg transition-all cursor-pointer border-none mb-4"
            >
              {loading ? "Resetting..." : "Reset Password"}
            </button>

            <p className="text-center text-sm text-gray-500 dark:text-gray-400">
              <Link to="/forgot-password" className="text-orange-500 hover:underline">
                Request a new reset link
              </Link>
            </p>
          </form>
        )}
      </div>
    </div>
  );
};

export default ResetPassword;

