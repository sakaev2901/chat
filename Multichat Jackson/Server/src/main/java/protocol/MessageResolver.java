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

    public void handleRequest(String jsonRequest) {
        Request request = new Request(jsonRequest);
        String command = request.getCommand();
        switch (command) {
            case "Login": {
//                this.loginService = new LoginServiceImpl();
                Response response = Response.build(loginService.login(request));
                user = (User) response.getData();
                clientHandler.sendMessage(response);
                break;
            }
            case "Message": {
//                this.messageService = new MessageServiceImpl();
                clientHandler.sendMessageAllClient(Response.build(messageService.sendMessage(request)));
                break;
            }
            case "get messages": {
//                this.paginationService = new PaginationServiceImpl();
                clientHandler.sendMessage(Response.build(paginationService.getMessages(request)));
                break;
            }
            case "set product": {
//                this.productService = new ProductServiceImpl();
                productService.addProduct(request);
                break;
            }
            case "set product to cart": {
//                this.cartService = new CartServiceImpl();
                cartService.addToCart(request, user.getId());
                break;
            }
            case "get cart": {
//                this.cartService = new CartServiceImpl();
                clientHandler.sendMessage(Response.build(cartService.getCart(request)));
                break;
            }
            case "get orders": {
//                this.orderService = new OrderServiceImpl();
                clientHandler.sendMessage(Response.build(orderService.getOrders(user.getId())));
                break;
            }
            case "buy cart": {
//                this.cartService = new CartServiceImpl();
                cartService.buyCart(user.getId());
                break;
            }
            case "clear cart": {
//                this.cartService = new CartServiceImpl();
                cartService.clearCart(user.getId());
                break;
            }
            case "get products": {
//                this.productService = new ProductServiceImpl();
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

    @Override
    public String getComponentName() {
        return "messageResolver";
    }
}