var webpackMerge = require('webpack-merge');
var commonConfig = require('./webpack.common.js');
module.exports = webpackMerge(commonConfig, {
    devtool: 'cheap-module-eval-source-map',
    output: {
        path: './target/test',
        filename: 'app.bundle.js',
        publicPath: '/sporing/'

    },
    devServer: {
        headers: {"Access-Control-Allow-Origin": "*"},
        historyApiFallback: {
            index: '/index.html',
        }
    }
});