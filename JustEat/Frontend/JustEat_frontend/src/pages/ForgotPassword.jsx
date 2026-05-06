import { useState } from "react";
import { Link } from "react-router-dom";
import { forgotPassword } from "../auth/authService";

const inputCls =
  "w-full px-3 py-2.5 border-2 border-gray-200 dark:border-gray-600 rounded-lg text-sm bg-white dark:bg-gray-700 text-gray-900 dark:text-white focus:outline-none focus:border-orange-500 transition-colors";
const labelCls =
  "text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider";

const ForgotPassword = () => {
  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess(false);
    setLoading(true);
    try {
      await forgotPassword(email);
      setSuccess(true);
    } catch (err) {
      setError(err.response?.data?.message || "Something went wrong. Please try again.");
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
          Enter your email to reset your password
        </p>

        {error && (
          <div className="bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-400 border border-red-200 dark:border-red-800 rounded-lg px-4 py-3 text-sm mb-4">
            {error}
          </div>
        )}

        {success ? (
          <div className="text-center">
            <div className="bg-green-50 dark:bg-green-900/20 text-green-700 dark:text-green-400 border border-green-200 dark:border-green-800 rounded-lg px-4 py-4 text-sm mb-6">
              <div className="text-2xl mb-2">✉️</div>
              <p className="font-semibold">Check your email!</p>
              <p className="mt-1 text-xs">
                If an account exists with this email, we&apos;ve sent a password reset link.
              </p>
            </div>
            <Link
              to="/login"
              className="text-orange-500 hover:underline text-sm font-semibold"
            >
              ← Back to Login
            </Link>
          </div>
        ) : (
          <form onSubmit={handleSubmit}>
            <div className="flex flex-col gap-1.5 mb-6">
              <label className={labelCls}>Email Address</label>
              <input
                type="email"
                placeholder="you@example.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                autoFocus
                required
                className={inputCls}
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-orange-500 hover:bg-orange-600 disabled:opacity-50 text-white font-semibold text-sm px-4 py-2.5 rounded-lg transition-all cursor-pointer border-none mb-4"
            >
              {loading ? "Sending..." : "Send Reset Link"}
            </button>

            <p className="text-center text-sm text-gray-500 dark:text-gray-400">
              Remember your password?{" "}
              <Link to="/login" className="text-orange-500 hover:underline">
                Sign In
              </Link>
            </p>
          </form>
        )}
      </div>
    </div>
  );
};

export default ForgotPassword;

