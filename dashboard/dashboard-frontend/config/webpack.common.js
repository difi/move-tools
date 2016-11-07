var webpack = require("webpack");
var path = require('path');
var HtmlWebpackPlugin = require("html-webpack-plugin");
module.exports = {
    entry: './src/main/frontend/app.js',
    module: {
        loaders: [
            {
                test: /\.html$/,
                loader: 'html'
            },
            // the url-loader uses DataUrls. 
            // the file-loader emits files. 
            {test: /\.(woff|woff2)$/, loader: "url-loader?limit=10000&mimetype=application/font-woff"},
            {test: /\.ttf$/, loader: "file-loader"},
            {test: /\.eot$/, loader: "file-loader"},
            {test: /\.svg$/, loader: "file-loader"}
        ]
    },
    plugins: [
        new webpack.HotModuleReplacementPlugin(),
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: "./src/main/frontend/index.html"
        }),
        new HtmlWebpackPlugin({
            filename: 'error/404.html',
            template: "./src/main/frontend/index.html"
        }),
        new webpack.ProvidePlugin({
           $: "jquery",
           jQuery: "jquery"
       })
    ]
};