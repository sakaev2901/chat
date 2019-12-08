package protocol;


import context.Component;
import models.User;
import servers.ClientHandler;
import services.*;

public class MessageResolver implements Component {
    private MessageService messageService;
    private LoginService loginService;
    private PaginationService paginationService;
    private ProductService productService;
    private CartService cartService;
    private OrderService orderService;

    private User user;
    private ClientHandler clientHandler;

    public MessageResolver(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public MessageResolver() {

    }

    public void handleRequest(String jsonRequest) {
        Request request = new Request(jsonRequest);
        String command = request.getCommand();
        System.out.println(command);
        switch (command) {
            case "Login": {
                Response response = Response.build(loginService.login(request));
//                user = (User) response.getData();
                clientHandler.sendMessage(response);
                break;
            }
            case "Message": {
                clientHandler.sendMessageAllClient(Response.build(messageService.sendMessage(request)));
                break;
            }
            case "get messages": {
                clientHandler.sendMessage(Response.build(paginationService.getMessages(request)));
                break;
            }
            case "set product": {
                productService.addProduct(request);
                break;
            }
            case "set product to cart": {
                cartService.addToCart(request);
                break;
            }
            case "get cart": {
                clientHandler.sendMessage(Response.build(cartService.getCart(request)));
                break;
            }
            case "get orders": {
                clientHandler.sendMessage(Response.build(orderService.getOrders(request)));
                break;
            }
            case "buy cart": {
                cartService.buyCart(request);
                break;
            }
            case "clear cart": {
                cartService.clearCart(request);
                break;
            }
            case "get products": {
                clientHandler.sendMessage(Response.build(productService.getProducts()));
            }
        }
    }


    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageServiceImpl messageService) {
        this.messageService = messageService;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public void setLoginService(LoginServiceImpl loginService) {
        this.loginService = loginService;
    }

    public PaginationService getPaginationService() {
        return paginationService;
    }

    public void setPaginationService(PaginationServiceImpl paginationService) {
        this.paginationService = paginationService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductServiceImpl productService) {
        this.productService = productService;
    }

    public CartService getCartService() {
        return cartService;
    }

    public void setCartService(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }


    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public String getComponentName() {
        return "messageResolver";
    }
}