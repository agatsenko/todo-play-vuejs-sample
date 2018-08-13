module.exports = {
  baseUrl: process.env.NODE_ENV === "production" ? "/todo/" : "/",
  css: {
    modules: false,
  }
}
